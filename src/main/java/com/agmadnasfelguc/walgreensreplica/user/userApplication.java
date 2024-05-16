package com.agmadnasfelguc.walgreensreplica.user;

import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.MessageCreator;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.TemplatePaths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class userApplication {

//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String, Message<Object>> kafkaTemplate){
//		System.out.println("NOO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//		return args -> {
//			ObjectMapper mapper = new ObjectMapper();
//			MessageCreator messageCreator = new MessageCreator(TemplatePaths.userLoginPath, new HashMap<>(), Map.of("email", "omarmmi2000@gmail.com", "password", "test123"));
//			ObjectNode message = (ObjectNode) messageCreator.createMessage();
//
//			kafkaTemplate.send(
//					MessageBuilder
//							.withPayload(mapper.writeValueAsString(message))
//							.setHeader(KafkaHeaders.REPLY_TOPIC, "fff")
//							.setHeader(KafkaHeaders.TOPIC, "userManagement")
//							.setHeader(KafkaHeaders.KEY, "UserLogin")
//							.build()
//			);
//
//		};
//	}

	public static void main(String[] args) {
		SpringApplication.run(userApplication.class, args);
	}
}
