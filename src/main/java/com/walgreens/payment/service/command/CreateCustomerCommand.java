package com.walgreens.payment.service.command;

import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.walgreens.payment.repository.CustomerRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CreateCustomerCommand implements Command{

    private UUID customerUuid;
    private String name;
    private String email;

    @Autowired
    private CustomerRepository customerRepository;


    Logger logger= LoggerFactory.getLogger(CreateCustomerCommand.class);


    @Override
    public void execute() {
        try {

            CustomerCreateParams.Builder customerCreateParamsBuilder =
                    CustomerCreateParams.builder()
                            .setName(name)
                            .setEmail(email)
                            .setBalance(0L);

            Customer customer = Customer.create(customerCreateParamsBuilder.build());
            customerRepository.create_customer(customerUuid, customer.getId());
            logger.info("Customer Created");

        } catch (StripeException e) {
            logger.error("Exception createCustomerCommand", e);

        }



    }

}
