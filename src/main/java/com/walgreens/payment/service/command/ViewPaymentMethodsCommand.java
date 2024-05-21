package com.walgreens.payment.service.command;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.walgreens.payment.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class ViewPaymentMethodsCommand implements Command {

    private UUID customerUuid;


    @Autowired
    private CustomerRepository customerRepository;

    Logger logger= LoggerFactory.getLogger(ViewPaymentMethodsCommand.class);


    @Override
    public void execute() {
        try {

            String customerId = customerRepository.get_customer(customerUuid);
            Customer customer = Customer.retrieve(customerId);
            System.out.println(customer.listPaymentMethods());
            logger.info("Viewing Payment Methods");

        }catch (StripeException e){
            logger.error("Exception viewPaymentMethodsCommand", e);
        }

    }


}
