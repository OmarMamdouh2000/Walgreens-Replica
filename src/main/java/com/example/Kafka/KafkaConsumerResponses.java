package com.example.Kafka;


import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.Final.CartTable;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerResponses {

	@KafkaListener(topics="cartResponses",groupId = "KafkaGroupResponse")
	public void consumeMessage(String message) {
		try{
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, HashMap.class);
			switch (data.get("commandName").toString()) {
				case "UpdateItemCountCommand":
					System.out.println("Response: "+data.get("data"));
					break;
				case "AddToSavedForLater":
					System.out.println("Response: "+data.get("data"));
					break;
				case "ReturnFromSavedForLater":
					System.out.println("Response: "+data.get("data"));
					break;
				case "GetUserCart":

					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						System.out.println("Response: "+cart.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "RemoveItem":
					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						System.out.println("Response: "+cart.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "ChangeOrderType":
					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						System.out.println("Response: "+cart.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "ApplyPromo":
					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						System.out.println("Response: "+cart.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "AddItem":
					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						System.out.println("Response: "+cart.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "AddComment":
					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						System.out.println("Response: "+cart.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				
			
				default:
					break;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
