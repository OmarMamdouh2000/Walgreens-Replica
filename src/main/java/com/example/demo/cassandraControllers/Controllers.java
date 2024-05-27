package com.example.demo.cassandraControllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.cassandraFirebase.FirebaseService;
import com.example.demo.cassandraJwt.JwtUtil;

import org.springframework.messaging.Message;


@RestController
public class Controllers {
	
	@Autowired
	private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;

	@Autowired
	FirebaseService firebaseService;
	// --------------------------------------------- CATEGORIES ------------------------------------------------
	
	@GetMapping("/listCategories")
	public Object listCategories()
	{	
		Map<String,Object> body = new HashMap<>();
		body.put("commandName", "listCategoriesCase");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		 RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
                    .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
                    .setHeader(KafkaHeaders.KEY, random)
                    .build());
					try{
						String payload = (String) result.get().getPayload();
					return payload;
					}catch(Exception e){
						return e.getMessage();
					}
	}
	
	@GetMapping("/getCategory/{categoryId}")
	public Object getCategory(@PathVariable Object categoryId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", categoryId);
		body.put("commandName", "getCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@GetMapping("/listCategoryProducts/{categoryId}")
	public Object listCategoryProducts(@PathVariable Object categoryId) 
	{ 
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", categoryId);
		body.put("commandName", "listCategoryProductsCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@DeleteMapping("/deleteCategory/{categoryId}")
	public String deleteCategory(@PathVariable Object categoryId, @RequestParam("sessionId") String sessionId)
	{
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", categoryId);
		body.put("commandName", "deleteCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@PostMapping("/addCategory")
	public String addCategory(@RequestBody Map<String, Object> body, @RequestParam("sessionId") String sessionId)
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		body.put("commandName", "addCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@PutMapping("/updateCategory/{categoryId}")
	public String updateCategory(@PathVariable Object categoryId, @RequestBody Map<String, Object> body, @RequestParam("sessionId") String sessionId)
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		body.put("parameter", categoryId);
		body.put("commandName", "updateCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	// --------------------------------------------- END CATEGORIES ------------------------------------------------
	
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	@GetMapping("/listProducts")
	public Object listProducts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
			int [] params = new int [2];
			params[0] = page;
			params[1] = size; //(List<Products>)
			
			Map<String,Object> body = new HashMap<>();
			body.put("parameter", params);
			body.put("commandName", "listProductsCase");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = null;
			try {
				jsonString = objectMapper.writeValueAsString(body);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				System.out.print(e.getMessage());
			}
			// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
			String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}

	@GetMapping("/getProduct/{productId}")
	public Object getProduct(@PathVariable Object productId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", productId);
		body.put("commandName", "getProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public String deleteProduct(@PathVariable Object productId, @RequestParam("sessionId") String sessionId) 
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", productId);
		body.put("commandName", "deleteProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	
	@PostMapping("/addProduct")
    public String addProduct(@RequestBody Map<String, Object> body, @RequestParam("image") MultipartFile image, @RequestParam("sessionId") String sessionId) 
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		body.put("commandName", "addProductCase");
		
		UUID randomId = Uuids.timeBased();
		String randomIdString = randomId.toString();
		try {
			firebaseService.uploadPhoto(randomIdString, image);
			body.put("image", randomIdString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
   	}
	
	@PutMapping("/updateProduct/{productId}")
	public String updateProduct(@PathVariable Object productId, @RequestBody Map<String, Object> body, @RequestParam("sessionId") String sessionId) 
	{
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		body.put("parameter", productId);
		body.put("commandName", "updateProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	
	// --------------------------------------------- BRANDS ------------------------------------------------

	@GetMapping("/listBrand")
	public Object ListBrand()
	{
		Map<String,Object> body = new HashMap<>();
		body.put("commandName", "listBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		// kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@GetMapping("/getBrand/{brandId}")
	public Object getBrand(@PathVariable Object brandId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", brandId);
		body.put("commandName", "getBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		//kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
		
	}
	
	@GetMapping("/listBrandProducts/{brandId}")
	public Object listBrandProducts(@PathVariable Object brandId) 
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", brandId);
		body.put("commandName", "listBrandProductsCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		//kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@DeleteMapping("/deleteBrand/{brandId}")
	public String deleteBrand(@PathVariable Object brandId, @RequestParam("sessionId") String sessionId)
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", brandId);
		body.put("commandName", "deleteBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		//kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	@PostMapping("/addBrand")
	public String addBrand(@RequestBody Map<String, Object> body, @RequestParam("sessionId") String sessionId)
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		body.put("commandName", "addBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		
		//kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	@PutMapping("/updateBrand/{brandId}")
	public String updateBrand(@PathVariable Object brandId, @RequestBody Map<String, Object> body, @RequestParam("sessionId") String sessionId)
	{
		
		String user = JwtUtil.getUserIdFromToken(sessionId);
		String[] adminArr = user.split(":");
		
		if(adminArr.length == 1 || !adminArr[1].equals("admin"))
		{
			return "You must be an Admin";
		}
		
		body.put("parameter", brandId);
		body.put("commandName", "updateBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		
		//kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
		String random=UUID.randomUUID().toString();
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
				   .withPayload(jsonString)
				   .setHeader(KafkaHeaders.REPLY_TOPIC, "ProductsResponses")
				   .setHeader(KafkaHeaders.TOPIC, "ProductsRequests")
				   .setHeader(KafkaHeaders.KEY, random)
				   .build());
				   try{
					   String payload = (String) result.get().getPayload();
				   return payload;
				   }catch(Exception e){
					   return e.getMessage();
				   }
	}
	
	// --------------------------------------------- END BRANDS ---------------------------------------------
}