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

        } catch (StripeException e) {
            log.error("Exception createCustomerCommand", e);

        }



    }

}
