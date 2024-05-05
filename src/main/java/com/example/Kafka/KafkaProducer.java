package com.example.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class KafkaProducer {
	@Autowired 
	KafkaTemplate<String, String> kafkaTemplate;
	
	public void publishToTopic(String topic,String message) {
		kafkaTemplate.send(topic,message);
		
	}

}
