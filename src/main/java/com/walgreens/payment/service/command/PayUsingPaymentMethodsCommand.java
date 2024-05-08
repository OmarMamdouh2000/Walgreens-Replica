package com.walgreens.payment.service.command;

import com.walgreens.payment.exceptions.InsufficientFundsException;
import com.walgreens.payment.repository.PaymentMethodsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private PaymentMethodsRepository paymentMethodsRepository;

    @Autowired
    private CreateATransactionCommand createATransactionCommand;




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
            createATransactionCommand.setTransactionTime(LocalDateTime.now());
            createATransactionCommand.execute();


        }catch(InsufficientFundsException e){
            log.error("ERROR: " + e.getMessage());
        }

    }


}
