package com.agmadnasfelguc.walgreensreplica.user.service.kafka;

import com.agmadnasfelguc.walgreensreplica.user.service.invoker.UserInvoker;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
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

    @KafkaListener(topics = "userManagement", groupId = "user")
    void listener(@Payload String message, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationIdBytes, @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic ) {
       ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(message);
            System.out.println("Received message: " + rootNode);

            // Check if rootNode is an ObjectNode to safely add properties
            if (rootNode instanceof ObjectNode objectNode) {

                // Convert correlation ID bytes to String
//                String correlationId = new String(correlationIdBytes);

                System.out.println("Correlation ID: " + correlationIdBytes);

                // Add the correlation ID to the JSON object
                objectNode.put("correlationId", correlationIdBytes);

                // Now pass the updated rootNode to the invoker
                userInvoker.callCommand(objectNode);
            } else {
                System.out.println("Root node is not an ObjectNode, cannot add correlation ID");
            }
        } catch (Exception e) {
            System.out.println("Error processing message: " + e.getMessage());
        }
    }

}
