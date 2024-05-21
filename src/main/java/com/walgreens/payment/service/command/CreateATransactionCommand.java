package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.TransactionsRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateATransactionCommand implements Command{

    private UUID transactionUuid;
    private UUID customerUuid;
    private UUID cartUuid;
    private UUID paymentMethodUuid;
    private String sessionId;
    private Timestamp transactionTime;
    private Double Amount;

    @Autowired
    private TransactionsRepository transactionsRepository;


    Logger logger= LoggerFactory.getLogger(CreateATransactionCommand.class);

    private double amount;





    @Override
    public void execute() {
        transactionUuid = UUID.randomUUID();
        transactionsRepository.create_a_transaction(
                transactionUuid,
                customerUuid,
                cartUuid,
                paymentMethodUuid,
                sessionId,
                transactionTime,
                Amount
        );
        logger.trace("Create a Transaction");




    }
}
