package com.example.demo.cassandraKafka;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraCommands.Invoker;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerRequests {
	@Autowired
	Invoker invoker=new Invoker();
	@Autowired
	KafkaProducer kafkaProducer;
	
	@KafkaListener(topics="ProductsRequests",groupId = "KafkaGroupRequest")
	public void consumeMessage(String message) {
		try {
			System.out.println("Request: "+message);
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, HashMap.class);
			Map<String,Object> result = new HashMap<>();
			result.put("commandName", data.get("commandName").toString());
			Object finalData="";
			switch (data.get("commandName").toString()) {
				

				case "listCategories":
					finalData= (Object) invoker.executeCommand("listCategoriesCommand", data.get("parameter"), data);
					break;
				case "getCategory":
					finalData= (Object) invoker.executeCommand("getCategoryCommand", data.get("parameter"), data);
					break;
				case "addCategory":
					finalData= (String) invoker.executeCommand("addCategoryCommand", null, data);
					break;
				default:
					break;
			}
			result.put("data", finalData);
			String response = objectMapper.writeValueAsString(result);
			kafkaProducer.publishToTopic("ProductsResponses", response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}