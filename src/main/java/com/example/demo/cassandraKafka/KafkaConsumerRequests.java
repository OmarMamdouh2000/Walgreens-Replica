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
				

				case "listCategoriesCase":
					finalData= (Object) invoker.executeCommand("listCategoriesCommand", data.get("parameter"), data);
					break;
				case "getCategoryCase":
					finalData= (Object) invoker.executeCommand("getCategoryCommand", data.get("parameter"), data);
					break;
				case "addCategoryCase":
					finalData= (String) invoker.executeCommand("addCategoryCommand", null, data);
					break;
				case "addBrandCase":
					finalData= (String) invoker.executeCommand("addBrandCommand", null, data);	
					break;
				case "getBrandCase":
					finalData= (Object) invoker.executeCommand("getBrandCOmmand", data.get("parameter"), data);
					break;
				case "listBrandCase":
					finalData= (Object) invoker.executeCommand("listBrandCommand",null, data);
					break;
				case "listBrandProductsCase":
					finalData= (Object) invoker.executeCommand("listBrandProductsCommand", data.get("parameter"), data);
					break;
				case "updateBrandCase":
					finalData= (String) invoker.executeCommand("updateBrandCommand", data.get("parameter"), data);
					break;
				case "deleteBrandCase":
					finalData= (String) invoker.executeCommand("deleteBrandCommand", data.get("parameter"), data);
					break;
				case "addProductCase":
					finalData= (String) invoker.executeCommand("addProductCommand", null, data);
					break;
				case "deleteProductCase":
					finalData= (String) invoker.executeCommand("deleteProductCommand", data.get("parameter"), data);
					break;
				case "getProductCase":
					finalData= (Object) invoker.executeCommand("getProductCOmmand", data.get("parameter"), data);
					break;
				case "listProductsCase":
					finalData= (Object) invoker.executeCommand("listProductsCommand", data.get("parameter"), data);
					break;
				case "updateProductCase":
					finalData= (String) invoker.executeCommand("updateProductommand", data.get("parameter"), data);
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