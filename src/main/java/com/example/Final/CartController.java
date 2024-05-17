package com.example.Final;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Claims;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.Cache.SessionCache;
import com.example.Commands.Invoker;
import com.example.Commands.JwtDecoderService;
import org.springframework.web.bind.annotation.*;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CartController {
	@Autowired
	private JwtDecoderService jwtDecoderService;
	@Autowired
	public Services cartService;
	@Autowired
	public Invoker invoker=new Invoker();
	@Autowired
	private KafkaProducer kafkaProducerRequest;


	@Autowired
	private SessionCache sessionCache;


	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	@GetMapping("/storeSessionUser")
	public void storeSessionUser(){
		// sessionCache.createSession("1234", "87033e74-dc2f-4672-87ba-6fdd0024d4d1", "user", "ziad@gmail", "ziad", "ziad");
		sessionCache.createSession("1234", "user", Map.of("userId", "e0036741-4913-499a-aad4-1c90d40b4a77", "email", "ziad@gmail", "username", "ziad"));
	}

	@GetMapping("/storeSessionCart")
	public void storeSessionCart(){
		// sessionCache.createSession("1234", "87033e74-dc2f-4672-87ba-6fdd0024d4d1", "user", "ziad@gmail", "ziad", "ziad");
		sessionCache.createSession("1234", "cart", Map.of("userId", "e0036741-4913-499a-aad4-1c90d40b4a77", "cartId", "cart1"));
	}
	@GetMapping("/cartSection")
	public Map<String, Object> getCartSection(){
		return sessionCache.getSessionSection("1234", "cart");
	}
	@GetMapping("/getSession")
	public Map<String, Object> getSession(){
		return sessionCache.getSessionSection("1234", "cart");

	}

	@GetMapping("/getCart")
	public Object getCart(@RequestParam String sessionId) {
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString( Map.of("userId", userId, "commandName", "GetUserCart", "sessionId", sessionId));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";
		//return invoker.executeCommand("GetUserCart", Map.of("User", claims.get("userId")));
	}

	@PostMapping("/editItemCount")
	public String editItemCount(@RequestParam String sessionId,@RequestBody Map<String,Object> data) {

		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "UpdateItemCountCommandCache");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";
		//return invoker.executeCommand("UpdateItemCountCommand", data).toString();

	}
	@PostMapping("/addItemToSavedLater")
	public String addItemToSavedLater(@RequestParam String sessionId,@RequestBody Map<String, Object> data) {
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "AddToSavedForLaterCache");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);

		return "success";
		//return invoker.executeCommand("AddToSavedForLater", data).toString();
	}
	@PostMapping("/returnItemFromSavedLater")
	public String returnItemFromSavedLater(@RequestParam String sessionId,@RequestBody Map<String, Object> data) {
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "ReturnFromSavedForLaterCache");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);

		return "success";
		//return invoker.executeCommand("ReturnFromSavedForLater", data).toString();
	}

	@PostMapping("/removeItem")
	public Object removeItemFromCart(@RequestParam String sessionId ,@RequestBody Map<String, Object> data) throws Exception {
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "RemoveItem");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";
	}

	@PostMapping("/changeOrderType")
	public Object setOrderType(@RequestParam String sessionId, @RequestBody Map<String, Object> data) throws Exception{
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "ChangeOrderType");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";

	}

	@PostMapping("/applyPromo")
	public Object applyPromo(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "ApplyPromo");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";

	}


	@PostMapping("/addItem")
	public Object addItem(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		//TODO: Publish To Product Service Kafka
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "AddItem");
		// Pobject pobject = new Pobject(UUID.fromString(""), "Product1", "url1", "brand1", 1.20, "10");
		// data.put("itemPrice", pobject.getPrice());
		// data.put("itemId", pobject.getId().toString());
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		kafkaProducerRequest.publishToTopic("productRequest",jsonString);
		return "success";

	}

	@PostMapping("/addComment")
	public Object addComment(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "AddComment");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";

	}

	@PostMapping("/proceedToCheckOut")
	public Object proceedToCheckOut(@RequestParam String token){
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString( Map.of("token", token, "commandName", "ProceedToCheckOutCommand"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";
	}
	@PostMapping("/confirmCheckout")
	public Object confirmCheckout(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String userId=sessionCache.getSessionSection(sessionId, "user").get("userId").toString();
		data.put("userId", userId);
		data.put("commandName", "ConfirmCheckoutCommand");
		try {
			jsonString = objectMapper.writeValueAsString( data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		kafkaProducerRequest.publishToTopic("cartRequests",jsonString);
		return "success";
	}

}
