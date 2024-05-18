package com.example.Kafka;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.Final.CartTable;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerResponses {
	@Autowired
	KafkaProducer kafkaProducer;

	@KafkaListener(topics="cartResponses",groupId = "KafkaGroupResponse")
	public void consumeMessage(String message) {
		try{
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, HashMap.class);
			switch (data.get("commandName").toString()) {
				case "UpdateItemCountCommand", "AddToSavedForLater", "ReturnFromSavedForLater",
                     "ProceedToCheckOutCommand", "ConfirmCheckoutCommand", "AddToSavedForLaterCache",
                     "ReturnFromSavedForLaterCache", "UpdateItemCountCommandCache", "UpdateCart":
					System.out.println("Response: "+data.get("data"));
					break;
                case "GetUserCart", "RemoveItem", "ChangeOrderType", "ApplyPromo", "AddItem", "AddComment":

					try{
						CartTable cart = objectMapper.convertValue(data.get("data"), CartTable.class);
						logger.info("Response: "+cart);
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
                default:
					break;
			}
		}catch(Exception e){
			logger.info(e.getMessage());
		}
	}

}
