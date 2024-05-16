package com.agmadnasfelguc.walgreensreplica.user.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);
    @Autowired
    private KafkaTemplate<String, Message<Object>> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, Message<Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Message<Object> message){
        kafkaTemplate.send(message);
        logger.info("Sending Message: " + message);
    }
}
