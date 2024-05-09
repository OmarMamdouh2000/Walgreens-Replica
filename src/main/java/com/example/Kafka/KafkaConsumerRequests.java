package com.example.Kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.Commands.Invoker;
import com.example.Final.OrderTable;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerRequests {
	@Autowired
	Invoker invoker=new Invoker();
	@Autowired
	KafkaProducer kafkaProducer;
	
	@KafkaListener(topics="orderRequests",groupId = "KafkaGroupRequest")
	public void consumeMessage(String message) {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			@SuppressWarnings("unchecked")
			Map<String ,Object>data = objectMapper.readValue(message, HashMap.class);

        switch (data.get("commandName").toString()) {
            case "GetOrdersCommand":
				List<OrderTable> orders = (List<OrderTable>) invoker.executeCommand("GetOrdersCommand", data);
				kafkaProducer.publishToTopic("orderResponses","orders successfully fetched");
				System.out.println(orders);
                break;
			case "GetActiveOrdersCommand":
				List<OrderTable> activeOrders = (List<OrderTable>) invoker.executeCommand("GetActiveOrdersCommand", data);
				kafkaProducer.publishToTopic("orderResponses","active orders successfully fetched");
				System.out.println(activeOrders);
				break;

			case "FilterOrders":
				String filteredOrders = (String)invoker.executeCommand("FilterOrders", data);
//				System.out.println(filteredOrders);
				kafkaProducer.publishToTopic("orderResponses",filteredOrders);
				break;
            default:
                break;
        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
