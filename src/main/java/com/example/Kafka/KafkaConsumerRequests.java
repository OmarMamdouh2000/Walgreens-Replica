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
			Object finalData =null;
			Map<String,Object> result=new HashMap<>();
			result.put("commandName",data.get("commandName").toString());


        switch (data.get("commandName").toString()) {
			case "CreateOrder":
				finalData = (String) invoker.executeCommand("CreateOrder", data);

				break;
            case "GetOrdersCommand":
				finalData = invoker.executeCommand("GetOrdersCommand", data);

                break;
			case "GetActiveOrdersCommand":
				finalData = invoker.executeCommand("GetActiveOrdersCommand", data);

				break;

			case "FilterOrders":
				finalData= invoker.executeCommand("FilterOrders", data);

				break;
			case "ConfirmRefund":
				finalData = (String)invoker.executeCommand("ConfirmRefund", data);

				break;
            default:
                break;
        }
		result.put("data",finalData);
		String response = objectMapper.writeValueAsString(result);
		kafkaProducer.publishToTopic("orderResponses", response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
