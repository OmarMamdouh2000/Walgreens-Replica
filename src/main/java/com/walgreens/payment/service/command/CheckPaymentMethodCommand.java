package com.walgreens.payment.service.command;

import com.walgreens.payment.model.ProductsDto;
import com.walgreens.payment.repository.PaymentMethodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Override
    public void execute() {
        UUID paymentMethodUuid = paymentMethodsRepository.get_default_payment_method(customerUuid);

        if (paymentMethodUuid != null) {
            PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand = new PayUsingPaymentMethodsCommand();
            payUsingPaymentMethodsCommand.setCustomerUuid(customerUuid);
            payUsingPaymentMethodsCommand.setPaymentMethodUuid(paymentMethodUuid);
            payUsingPaymentMethodsCommand.setAmount(amount);
            payUsingPaymentMethodsCommand.execute();
        } else{
            CreateCheckoutCommand createCheckoutCommand = new CreateCheckoutCommand();
            createCheckoutCommand.setCustomerUuid(customerUuid);
            createCheckoutCommand.setCartUuid(cartUuid);
            createCheckoutCommand.execute();
        }
    }
}
