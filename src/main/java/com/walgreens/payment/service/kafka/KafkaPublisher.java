package com.walgreens.payment.service.kafka;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

@Service
public class KafkaPublisher {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(String topic, String message){
        kafkaTemplate.send(topic, message);
    }
}
