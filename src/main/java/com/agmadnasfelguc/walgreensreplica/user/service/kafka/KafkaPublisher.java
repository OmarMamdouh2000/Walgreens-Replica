package com.agmadnasfelguc.walgreensreplica.user.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {

    @Autowired
    private KafkaTemplate<String, Message<Object>> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, Message<Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Message<String> message){
        kafkaTemplate.send(message);
    }
}
