package com.walgreens.payment.service.kafka.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class UserRequests {

    @Autowired
    private ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate;

    public JsonNode sendAndReceiveRequest(String jsonString){

        ObjectMapper mapper = new ObjectMapper();
        Message message = MessageBuilder.withPayload(jsonString)
                .setHeader(KafkaHeaders.TOPIC, "payment")
                .setHeader(KafkaHeaders.REPLY_TOPIC, "responseTopic")
                .build();
        try{
            RequestReplyMessageFuture<String, String> result = replyKafkaTemplate.sendAndReceive(message);
            String payload = (String) result.get().getPayload();
            return mapper.readTree(payload);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
