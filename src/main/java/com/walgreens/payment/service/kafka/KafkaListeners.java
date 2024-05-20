package com.walgreens.payment.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.walgreens.payment.service.invoker.PaymentInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaListeners {

    @Autowired
    private PaymentInvoker paymentInvoker;

    @KafkaListener(topics = "payment", groupId = "payment")
    void listener(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            JsonNode rootNode = objectMapper.readTree(message);
            System.out.println("Received message");
            System.out.println(rootNode);
            paymentInvoker.callCommand(rootNode);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

//    @KafkaListener(topics = "payment", groupId = "payment")
//    void listener(@Payload String message, @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try{
//            JsonNode rootNode = objectMapper.readTree(message);
//            if(rootNode instanceof ObjectNode objectNode){
//                objectNode.put("replyTopic", replyTopic);
//                paymentInvoker.callCommand(objectNode);
//            }else{
//                System.out.println("Root node is not an ObjectNode, cannot add reply topic");
//            }
//        }catch (JsonProcessingException e){
//            System.out.println(e.getMessage());
//        }
//    }


}
