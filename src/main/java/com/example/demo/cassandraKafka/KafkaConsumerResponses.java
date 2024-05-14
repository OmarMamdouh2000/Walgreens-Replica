package com.example.demo.cassandraKafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraModels.Categories;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerResponses {

	@KafkaListener(topics="ProductsResponses",groupId = "KafkaGroupResponse")
	public void consumeMessage(String message) {
		try{
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, HashMap.class);
			switch (data.get("commandName").toString()) {
				case "listCategories":
					try{
						@SuppressWarnings("unchecked")
						List<Categories> category = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + category.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "getCategory":
					try{
						Categories category = objectMapper.convertValue(data.get("data"), Categories.class);
						System.out.println("Response: " + category.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "addCategory":
					System.out.println("Response: "+data.get("data"));
					break;
				default:
					break;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}