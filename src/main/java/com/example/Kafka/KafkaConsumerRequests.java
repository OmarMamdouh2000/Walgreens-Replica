package com.example.Kafka;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.Commands.Invoker;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerRequests {
	@Autowired
	Invoker invoker=new Invoker();
	@Autowired
	KafkaProducer kafkaProducer;
	
	@KafkaListener(topics="cartRequests",groupId = "KafkaGroupRequest")
	public void consumeMessage(String message) {
		try {
			System.out.println("Request: "+message);
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, HashMap.class);

			switch (data.get("commandName").toString()) {
				case "UpdateItemCountCommand":
					String result= (String) invoker.executeCommand("UpdateItemCountCommand", data);
					kafkaProducer.publishToTopic("cartResponses",result);
					
					break;
				case "AddToSavedForLater":
					String result1= (String) invoker.executeCommand("AddToSavedForLater", data);
					kafkaProducer.publishToTopic("cartResponses",result1);
					break;
				case "ReturnFromSavedForLater":
					String result2= (String) invoker.executeCommand("ReturnFromSavedForLater", data);
					kafkaProducer.publishToTopic("cartResponses",result2);
					break;
				
			
				default:
					break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
