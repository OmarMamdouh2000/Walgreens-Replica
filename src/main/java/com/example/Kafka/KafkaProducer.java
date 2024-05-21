package com.example.Kafka;

import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Service
public class KafkaProducer {
	@Autowired 
	KafkaTemplate<String, Message<String>> kafkaTemplate;
	
	public void publishToTopic(String topic,String message,byte[] correlationId) {
		Message<String> kafkaMessage = MessageBuilder
                    .withPayload(message)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
					.setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();

		kafkaTemplate.send(kafkaMessage);
		
	}

}
