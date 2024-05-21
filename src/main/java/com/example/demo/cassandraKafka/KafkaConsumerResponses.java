package com.example.demo.cassandraKafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger logger = LoggerFactory.getLogger(KafkaConsumerRequests.class);

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
						logger.info("Response: " + categories.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "getCategoryCase":
					try{
						Categories category = objectMapper.convertValue(data.get("data"), Categories.class);
						logger.info("Response: " + category.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "listCategoryProductsCase":
					try{
						@SuppressWarnings("unchecked")
						List<Pobject> products = objectMapper.convertValue(data.get("data"), List.class);
						logger.info("Response: " + products.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "deleteCategoryCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "addCategoryCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "updateCategoryCase":
					logger.info("Response: "+data.get("data"));
					break;
				
				//Products
				case "listProductsCase":
					try{
						@SuppressWarnings("unchecked")
						List<Products> products = objectMapper.convertValue(data.get("data"), List.class);
						logger.info("Response: " + products.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "getProductCase":
					try{
						logger.info(((Products)data.get("data")).toString());
						Products product = objectMapper.convertValue(data.get("data"), Products.class);
						logger.info("Response: " + product.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "deleteProductCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "addProductCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "updateProductCase":
					logger.info("Response: "+data.get("data"));
					break;
					
				// Brands
				case "listBrandCase":
					try {
						@SuppressWarnings("unchecked")
						List<Brands> brands = objectMapper.convertValue(data.get("data"), List.class);
						logger.info("Response: " + brands.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "getBrandCase":
					try{
						Brands brand = objectMapper.convertValue(data.get("data"), Brands.class);
						logger.info("Response: " + brand.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "listBrandProductsCase":
					try {
						@SuppressWarnings("unchecked")
						List<Pobject> products = objectMapper.convertValue(data.get("data"), List.class);
						logger.info("Response: " + products.toString());
					}catch(Exception e){
						String error = (String)data.get("data");
						logger.info("Response: "+error);
					}
					break;
				case "deleteBrandCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "addBrandCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "updateBrandCase":
					logger.info("Response: "+data.get("data"));
					break;
				case "GetProductForCartCommand":
					try{
						Products product = objectMapper.convertValue(data.get("data"), Products.class);
						logger.info("Response: " + product.toString());
						data.put("itemPrice", product.getPrice());
						
						if(product.getDiscount()!="" && !product.getDiscount().isEmpty())
							data.put("discount", Double.parseDouble(product.getDiscount()));
						else
							data.put("discount", 0.0);

						data.put("itemName", product.getName());
						kafkaProducer.publishToTopic("cartRequests", objectMapper.writeValueAsString(data));
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