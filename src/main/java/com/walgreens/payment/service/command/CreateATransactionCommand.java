package com.walgreens.payment.service.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgreens.payment.repository.TransactionsRepository;
import com.walgreens.payment.service.kafka.KafkaPublisher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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
    private String sessionIdCart;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    KafkaPublisher kafkaPublisher;

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
        Map<String,Object> map = new HashMap<>();
        map.put("transactionNumber",transactionUuid);
       map.put("sessionId", sessionIdCart);
        map.put("userId", customerUuid);
        map.put("commandName", "ConfirmCheckoutCommand");
        ObjectMapper objectMapper = new ObjectMapper();
        String result="";
        try {
            result = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        kafkaPublisher.publish("cartRequests", result);
        logger.trace("Create a Transaction");




    }
}
