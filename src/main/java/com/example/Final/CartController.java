package com.example.Final;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import io.jsonwebtoken.Claims;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.example.Cache.SessionCache;
import com.example.Commands.Invoker;
import com.example.Commands.JwtDecoderService;
import org.springframework.web.bind.annotation.*;

import com.example.Kafka.CartFormulator;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;

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
	private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;

	@Autowired
	private CartFormulator cartFormulator;
	@Autowired
	private SessionCache sessionCache;
	@GetMapping("/getToken")
	public String getToken(@RequestParam String userId) {
		return jwtDecoderService.generateToken(userId);
	}
	@GetMapping("/getUserId")
	public String getUserId(@RequestParam String sessionId) {
		System.out.println("JWT DECODE RESULT: " + jwtDecoderService.getUserIdFromToken(sessionId));
		return jwtDecoderService.getUserIdFromToken(sessionId);
	}


	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	// @GetMapping("/storeSessionUser")
	// public void storeSessionUser(){						
	// 	// sessionCache.createSession("1234", "87033e74-dc2f-4672-87ba-6fdd0024d4d1", "user", "ziad@gmail", "ziad", "ziad");
	// 	sessionCache.createSession("1234", "user", Map.of("userId", "235772bd-f0de-41ee-8280-642a5bdf837f", "email", "ziad@gmail", "username", "ziad"));
	// }

	@GetMapping("/storeSessionCart")
	public void storeSessionCart(){
		// sessionCache.createSession("1234", "87033e74-dc2f-4672-87ba-6fdd0024d4d1", "user", "ziad@gmail", "ziad", "ziad");
		sessionCache.createSession("1234", "cart", Map.of("userId", "235772bd-f0de-41ee-8280-642a5bdf837f", "cartId", "cart1"));
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
	public Object getCart(@RequestParam String sessionId) throws InterruptedException, ExecutionException {
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString( Map.of("userId", userId, "commandName", "GetUserCart", "sessionId", sessionId,"correlationId",random));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		 RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
			return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}

	@PostMapping("/editItemCount")
	public String editItemCount(@RequestParam String sessionId,@RequestBody Map<String,Object> data) {

		String userId=jwtDecoderService.getUserIdFromToken(sessionId);;
		if(userId==null){
			return "Invalid Token";
		}
		data.put("userId", userId);
		data.put("commandName", "UpdateItemCountCommandCache");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
		String random=UUID.randomUUID().toString();
		data.put("correlationId", random);
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return (String)cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
		//return invoker.executeCommand("UpdateItemCountCommand", data).toString();

	}
	@PostMapping("/addItemToSavedLater")
	public String addItemToSavedLater(@RequestParam String sessionId,@RequestBody Map<String, Object> data) {
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("userId", userId);
		data.put("commandName", "AddToSavedForLaterCache");
		data.put("sessionId", sessionId);
		String random=UUID.randomUUID().toString();
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return (String)cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
		//return invoker.executeCommand("AddToSavedForLater", data).toString();
	}
	@PostMapping("/returnItemFromSavedLater")
	public String returnItemFromSavedLater(@RequestParam String sessionId,@RequestBody Map<String, Object> data) {
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("userId", userId);
		data.put("commandName", "ReturnFromSavedForLaterCache");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
		String random=UUID.randomUUID().toString();
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return (String)cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
		//return invoker.executeCommand("ReturnFromSavedForLater", data).toString();
	}

	@PostMapping("/removeItem")
	public Object removeItemFromCart(@RequestParam String sessionId ,@RequestBody Map<String, Object> data) throws Exception {
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("sessionId", sessionId);
		data.put("userId", userId);
		data.put("commandName", "RemoveItem");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}

	@PostMapping("/changeOrderType")
	public Object setOrderType(@RequestParam String sessionId, @RequestBody Map<String, Object> data) throws Exception{
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("sessionId", sessionId);
		data.put("userId", userId);
		data.put("commandName", "ChangeOrderType");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}

	}

	@PostMapping("/applyPromo")
	public Object applyPromo(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("sessionId", sessionId);
		data.put("userId", userId);
		data.put("commandName", "ApplyPromo");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}

	}


	@PostMapping("/addItem")
	public Object addItem(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("sessionId", sessionId);
		data.put("userId", userId);
		data.put("commandName", "GetProductForCartCommand");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}

	}

	@PostMapping("/addComment")
	public Object addComment(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("sessionId", sessionId);
		data.put("userId", userId);
		data.put("commandName", "AddComment");
		ObjectMapper objectMapper = new ObjectMapper();
		String random=UUID.randomUUID().toString();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}

	@PostMapping("/proceedToCheckOut")
	public Object proceedToCheckOut(@RequestParam String sessionId){
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString( Map.of("sessionId", sessionId,"userId",userId, "commandName", "ProceedToCheckOutCommand"));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}
	@PostMapping("/confirmCheckout")
	public Object confirmCheckout(@RequestParam String sessionId, @RequestBody Map<String, Object> data){
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}
		data.put("userId", userId);
		data.put("commandName", "ConfirmCheckoutCommand");
		String random=UUID.randomUUID().toString();
		try {
			jsonString = objectMapper.writeValueAsString( data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
                    .setHeader(KafkaHeaders.TOPIC, "cartRequests")
                    .setHeader(KafkaHeaders.KEY, random)
					//.setHeader(KafkaHeaders.CORRELATION_ID, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
		return cartFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}

}
