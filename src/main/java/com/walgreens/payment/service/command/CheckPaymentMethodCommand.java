package com.walgreens.payment.service.command;

import com.walgreens.payment.model.ProductsDto;
import com.walgreens.payment.repository.PaymentMethodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckPaymentMethodCommand implements Command {

    private UUID customerUuid;
    private UUID cartUuid;
    private double amount;
    private UUID paymentMethodUuid;
    private List<ProductsDto> productsList = new ArrayList<>();

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void execute() {
        boolean hasPaymentMethods = paymentMethodsRepository.has_funds_default_payment_method(customerUuid);
        if (hasPaymentMethods) {
            UUID paymentMethodUuid= (UUID)paymentMethodsRepository.get_default_payment_method(customerUuid) ;
            PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand = new PayUsingPaymentMethodsCommand();
            payUsingPaymentMethodsCommand.setCustomerUuid(customerUuid);
            payUsingPaymentMethodsCommand.setPaymentMethodUuid(paymentMethodUuid);
            payUsingPaymentMethodsCommand.setAmount(amount);
            payUsingPaymentMethodsCommand.execute();
        } else{
            System.out.println(customerUuid);
//             String className = "com.walgreens.payment.service.command.CreateCheckoutCommand"; // replace with your class name
//             Class<?> class1 = Class.forName(className);
//             Constructor<?> constructor = class1.getDeclaredConstructor(CartRepo.class, JwtDecoderService.class , PromoRepo.class, UserUsedPromoRepo.class,KafkaProducer.class,SessionCache.class); // replace with your parameter types
//             Object instance = constructor.newInstance(cartRepo, jwtDecoderService, promoRepo, userUsedPromoRepo,kafkaProducer,sessionCache); // replace with your actual parameters

//             // If your class has a method you want to invoke, you can do so like this:
//             String methodName = "execute"; // replace with your method name
//             Method method = class1.getDeclaredMethod(methodName,  Map.class); // replace with your method parameters
// //                System.out.println(data);
//             return method.invoke(instance, data);

            // CreateCheckoutCommand createCheckoutCommand = new CreateCheckoutCommand();
            CreateCheckoutCommand createCheckoutCommand = (CreateCheckoutCommand)applicationContext.getBean("createCheckoutCommand");
            createCheckoutCommand.setCustomerUuid(customerUuid);
            createCheckoutCommand.setCartUuid(cartUuid);
            createCheckoutCommand.execute();
        }
    }
}
