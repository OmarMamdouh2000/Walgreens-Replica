package com.example.demo.cassandraKafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
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
				case "listCategoriesCase":
					try{
						@SuppressWarnings("unchecked")
						List<Categories> category = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + category.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "getCategoryCase":
					try{
						Categories category = objectMapper.convertValue(data.get("data"), Categories.class);
						System.out.println("Response: " + category.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "addCategoryCase":
					System.out.println("Response: "+data.get("data"));
					break;
					
				case "addBrandCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "getBrandCase":
					try{
						Brands brand = objectMapper.convertValue(data.get("data"), Brands.class);
						System.out.println("Response: " + brand.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					
					break;
				case "listBrandCase":
					try {
					List<Brands> brand = objectMapper.convertValue(data.get("data"), List.class);
					System.out.println("Response: " + brand.toString());
					}catch(Exception e){
					String error = (String)data.get("data");
					System.out.println("Response: "+error);
					}
					break;
				
				case "listBrandProductsCase":
					try {
						List<Pobject> products = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + products.toString());
						}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
						}
						break;	
				case "updateBrandCase":
					System.out.println("Response: "+data.get("data"));
					break;
					
				case "deleteBrandCase":
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