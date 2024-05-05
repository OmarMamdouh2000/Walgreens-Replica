package com.example.Kafka;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.Commands.Invoker;

@Service
public class KafkaConsumerRequests {
	@Autowired
	Invoker invoker=new Invoker();
	@Autowired
	KafkaProducer kafkaProducer;
	
	@KafkaListener(topics="cartRequests2",groupId = "KafkaGroupRequest")
	public void consumeMessage(String message) {
		System.out.println("Request: "+message);
		try {
			JSONObject jsonObject = new JSONObject("{\"itemId\":\"436d22c6-fe21-4ca9-83f4-d3ae9e98c0b0\",\"commandName\":\"UpdateItemCountCommand\",\"token\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI4NzAzM2U3NC1kYzJmLTQ2NzItODdiYS02ZmRkMDAyNGQ0ZDEiLCJpYXQiOjE3MTQ4NTE1NjcsImV4cCI6MTcxNDkzNzk2N30.TBp6TRj2Fmcj-Hr1GGH02SQ55XP7gutHetgXtaIeXyY\",\"itemCount\":1}");
			Map<String, Object> map = jsonObject.toMap();
        switch (jsonObject.getString("commandName")) {
            case "UpdateItemCountCommand":
				String result= (String) invoker.executeCommand("UpdateItemCountCommand", map);
				kafkaProducer.publishToTopic("cartResponses2",result);
                
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
