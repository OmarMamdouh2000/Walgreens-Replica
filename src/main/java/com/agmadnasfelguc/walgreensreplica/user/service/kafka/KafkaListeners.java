package com.agmadnasfelguc.walgreensreplica.user.service.kafka;

import com.agmadnasfelguc.walgreensreplica.user.UserApplication;
import com.agmadnasfelguc.walgreensreplica.user.service.invoker.UserInvoker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;



@Service
public class KafkaListeners {
    @Autowired
    private UserInvoker userInvoker;

    Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    @KafkaListener(topics = "userManagement", groupId = "user")
    void listener(@Payload String message, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationIdBytes, @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic ) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(message);
            System.out.println("Received message: " + rootNode);
            if (rootNode instanceof ObjectNode objectNode) {
                objectNode.put("correlationId", correlationIdBytes);
                objectNode.put("replyTopic", replyTopic);
                logger.info("Received message" + rootNode);
                userInvoker.callCommand(objectNode);
            } else {
                System.out.println("Root node is not an ObjectNode, cannot add correlation ID");
            }
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
            logger.error(e.getMessage());
        }
    }


}
