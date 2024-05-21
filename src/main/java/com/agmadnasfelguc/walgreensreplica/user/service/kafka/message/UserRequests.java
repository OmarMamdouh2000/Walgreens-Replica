package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message;

import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JsonReader;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.KafkaListeners;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.KafkaPublisher;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.MessageCreator;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.TemplatePaths;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class UserRequests {

    @Autowired
    private KafkaPublisher publisher;
    Logger logger = LoggerFactory.getLogger(UserRequests.class);

    @Autowired
    private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;


    public void sendRequest(String templatePath, String replyTopic, Map<String,String> paramsMap, Map<String,String> bodyMap) {
        MessageCreator mc = new MessageCreator(templatePath, paramsMap, bodyMap);
        JsonNode message = mc.createMessage();
        ObjectMapper mapper = new ObjectMapper();
        try {
            publisher.publish(
                    MessageBuilder
                            .withPayload(mapper.writeValueAsString(message))
                            .setHeader(KafkaHeaders.REPLY_TOPIC, replyTopic)
                            .setHeader(KafkaHeaders.TOPIC, "userManagement")
                            .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                            .build()
            );
            logger.info("Sending Message: " + message);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
    }

    public void publishResponse(String topic, byte[] correlationId, ResponseStatus responseStatus, Map<String, Object> payload) {
        try {
            JsonNode root = JsonReader.readJsonFromResourcesAsJson(TemplatePaths.responsePath);
            ((ObjectNode) root).put("status", responseStatus.getStatus().toString());
            ((ObjectNode) root).put("message", responseStatus.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            if (payload != null)
                ((ObjectNode) root).putPOJO("payload", payload);
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(mapper.writeValueAsString(root))
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                    .build();
            if (correlationId != null) {
                kafkaMessage = MessageBuilder
                        .withPayload(mapper.writeValueAsString(root))
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                        .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                        .build();
            }
            System.out.println("printing here");
            System.out.println(kafkaMessage);
            publisher.publish(kafkaMessage);
            logger.info("Published response: " + kafkaMessage);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    public JsonNode sendAndReceiveRequest(String templatePath,Map<String,String> paramsMap, Map<String,String> bodyMap){
        MessageCreator mc = new MessageCreator(templatePath, paramsMap, bodyMap);
        JsonNode message = mc.createMessage();
        ObjectMapper mapper = new ObjectMapper();
        try {
            RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(mapper.writeValueAsString(message))
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "responseTopic")
                    .setHeader(KafkaHeaders.TOPIC, "userManagement")
                    .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
                    .build());
            logger.info("Sent Message: " + message + "and received: " + result.get().getPayload());
            String payload = (String) result.get().getPayload();
            return mapper.readTree(payload);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }



}
