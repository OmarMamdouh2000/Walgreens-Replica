package com.example.demo.cassandraKafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraModels.Products;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerResponses {
	@Autowired
	public KafkaProducer kafkaProducer;
	
	@KafkaListener(topics="ProductsResponses",groupId = "KafkaGroupResponseProduct")
	public void consumeMessage(String message) {
		try{
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, Map.class);
			switch (data.get("commandName").toString()) {
				case "listCategoriesCase":
					try{
						@SuppressWarnings("unchecked")
						List<Categories> categories = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + categories.toString());
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
				case "listCategoryProductsCase":
					try{
						@SuppressWarnings("unchecked")
						List<Pobject> products = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + products.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "deleteCategoryCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "addCategoryCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "updateCategoryCase":
					System.out.println("Response: "+data.get("data"));
					break;
				
				//Products
				case "listProductsCase":
					try{
						@SuppressWarnings("unchecked")
						List<Products> products = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + products.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "getProductCase":
					try{
						System.out.println(data.get("data"));
						Products product = objectMapper.convertValue(data.get("data"), Products.class);
						System.out.println("Response: " + product.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "deleteProductCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "addProductCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "updateProductCase":
					System.out.println("Response: "+data.get("data"));
					break;
					
				// Brands
				case "listBrandCase":
					try {
						@SuppressWarnings("unchecked")
						List<Brands> brands = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + brands.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
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
				case "listBrandProductsCase":
					try {
						@SuppressWarnings("unchecked")
						List<Pobject> products = objectMapper.convertValue(data.get("data"), List.class);
						System.out.println("Response: " + products.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						System.out.println("Response: "+error);
					}
					break;
				case "deleteBrandCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "addBrandCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "updateBrandCase":
					System.out.println("Response: "+data.get("data"));
					break;
				case "GetProductForCartCommand":
					try{
						Products product = objectMapper.convertValue(data.get("data"), Products.class);
						System.out.println("Response: " + product.toString());
						data.put("itemPrice", product.getPrice());
						
						if(product.getDiscount()!="" && !product.getDiscount().isEmpty())
							data.put("discount", Double.parseDouble(product.getDiscount()));
						else
							data.put("discount", 0.0);

						data.put("itemName", product.getName());
						System.out.println(data);
						kafkaProducer.publishToTopic("cartRequests", objectMapper.writeValueAsString(data));
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