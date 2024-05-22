package com.walgreens.payment.service.command;

import com.walgreens.payment.exceptions.InsufficientFundsException;
import com.walgreens.payment.model.CartItem;
import com.walgreens.payment.repository.CustomerRepository;
import com.walgreens.payment.repository.PaymentMethodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import  java.util.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PayUsingPaymentMethodsCommand implements Command {

    private UUID customerUuid;
    private UUID cartUuid;
    private UUID paymentMethodUuid;
    private Double amount;

    private List<CartItem> cartItems;

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;



    @Autowired
    private CreateATransactionCommand createATransactionCommand;

    @Autowired
    private ApplicationContext applicationContext;


    Logger logger= LoggerFactory.getLogger(PayUsingPaymentMethodsCommand.class);



    @Override
    public void execute()  {
        try{


            boolean hasFunds = paymentMethodsRepository.check_has_funds(paymentMethodUuid);

            if(!hasFunds){
                throw new InsufficientFundsException("InsufficientFunds");
            }

            createATransactionCommand.setPaymentMethodUuid(paymentMethodUuid);
            createATransactionCommand.setCustomerUuid(customerUuid);
            createATransactionCommand.setCartUuid(cartUuid);
            createATransactionCommand.setAmount(amount);
            createATransactionCommand.setTransactionTime(Timestamp.from(Instant.now()));
            createATransactionCommand.execute();

            logger.info("Payment Methods");



        }catch(InsufficientFundsException e){
            logger.error("ERROR: " + e.getMessage());
        }

    }


}
