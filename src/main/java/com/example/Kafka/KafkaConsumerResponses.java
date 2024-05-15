package com.example.Kafka;


import com.example.Final.CartTable;
import com.example.Final.OrderController;
import com.example.Final.OrderTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KafkaConsumerResponses {

	@KafkaListener(topics="orderResponses",groupId = "KafkaGroupResponse")
	public void consumeMessage(String message) {
		try {
			message = message.replace("\\", "");
			message = message.substring(1, message.length() - 1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> data = objectMapper.readValue(message, HashMap.class);
			switch (data.get("commandName").toString()) {
				case "FilterOrders", "GetOrdersCommand", "GetActiveOrdersCommand":
					try{
						List<OrderTable> filteredOrders = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: "+filteredOrders);
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "CreateOrder":
					String result=(String) data.get("data");
					System.out.println("Response: "+result);

				default:
					break;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
