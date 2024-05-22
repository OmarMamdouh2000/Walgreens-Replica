package com.walgreens.payment.service.command;

import com.walgreens.payment.model.CartItem;
import com.walgreens.payment.model.ProductsDto;
import com.walgreens.payment.repository.CustomerRepository;
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
    private List<CartItem> cartItems;

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void execute() {

        boolean customerExists = customerRepository.check_customer_exists(customerUuid);

        if(!customerExists){
            CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand) applicationContext.getBean("createCustomerCommand");
            createCustomerCommand.setCustomerUuid(customerUuid);
            createCustomerCommand.execute();
        }

        boolean hasPaymentMethods = paymentMethodsRepository.has_funds_default_payment_method(customerUuid);
        if (hasPaymentMethods) {
            UUID paymentMethodUuid= UUID.fromString(paymentMethodsRepository.get_default_payment_method(customerUuid));
            PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand =(PayUsingPaymentMethodsCommand) applicationContext.getBean("payUsingPaymentMethodsCommand");
            payUsingPaymentMethodsCommand.setCustomerUuid(customerUuid);
            payUsingPaymentMethodsCommand.setCartUuid(cartUuid);
            payUsingPaymentMethodsCommand.setPaymentMethodUuid(paymentMethodUuid);
            payUsingPaymentMethodsCommand.setAmount(amount);
            payUsingPaymentMethodsCommand.execute();
        } else{
            System.out.println(customerUuid);
            CreateCheckoutCommand createCheckoutCommand = (CreateCheckoutCommand)applicationContext.getBean("createCheckoutCommand");
            createCheckoutCommand.setCustomerUuid(customerUuid);
            createCheckoutCommand.setCartUuid(cartUuid);
            createCheckoutCommand.setCartItems(cartItems);
            createCheckoutCommand.execute();
        }
    }
}
