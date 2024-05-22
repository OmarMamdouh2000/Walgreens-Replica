package com.example.Final;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.Cache.SessionCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Commands.Invoker;
import com.example.Commands.JwtDecoderService;
import com.example.Kafka.KafkaProducer;
import com.example.Kafka.OrderFormulator;
import com.example.Kafka.StringFormulator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@RestController
public class OrderController {
	@Autowired
	private SessionCache sessionCache;

	@GetMapping("/error")
	public String error() {
		return "error";
	}
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	@Autowired 
	Invoker invoker;
	@Autowired
	public OrderRepo orderRepo;

	

	@Autowired
	public KafkaProducer kafkaProducer;

	@Autowired
	private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;

	@Autowired
	private OrderFormulator orderFormulator;
	@Autowired
	private StringFormulator stringFormulator;

	@Autowired
	private JwtDecoderService jwtDecoderService;

	@PostMapping("/addOrder")
	public void addOrder(@RequestBody OrderTable data) {
		System.out.println(data);
		orderRepo.save(data);
	}

	
	@PostMapping("/filterOrders")
	public Object filterOrders(@RequestParam String sessionId, @RequestBody Map<String,Object> data) {
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}

		data.put("userId", userId);
		data.put("commandName", "FilterOrders");
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
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "orderResponses")
                    .setHeader(KafkaHeaders.TOPIC, "orderRequests")
                    .setHeader(KafkaHeaders.KEY, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
			return orderFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}
	
	@GetMapping("/getOrders")
	public Object getOrders(@RequestParam String sessionId) {
		
		Map<String,Object> data = new HashMap<String, Object>();

		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}

		data.put("userId", userId);
		data.put("commandName", "GetOrdersCommand");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
		String random=UUID.randomUUID().toString();
		data.put("correlationId", random);
		try {
			jsonString = objectMapper.writeValueAsString( data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		 RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "orderResponses")
                    .setHeader(KafkaHeaders.TOPIC, "orderRequests")
                    .setHeader(KafkaHeaders.KEY, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
			
			return (List<OrderTable>)orderFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
		
	}
	@GetMapping("/getActiveOrders")
	public Object getActiveOrders(@RequestParam String sessionId) {
		Map<String,Object> data = new HashMap<String, Object>();

		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}

		data.put("userId", userId);
		data.put("commandName", "GetActiveOrdersCommand");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
		String random=UUID.randomUUID().toString();
		data.put("correlationId", random);
		try {
			jsonString = objectMapper.writeValueAsString( data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		 RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "orderResponses")
                    .setHeader(KafkaHeaders.TOPIC, "orderRequests")
                    .setHeader(KafkaHeaders.KEY, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
			return orderFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
		
	}

	@PostMapping("/refund")
	public Object requestRefund(@RequestParam String sessionId, @RequestBody Map<String,Object> data) {
		String userId=jwtDecoderService.getUserIdFromToken(sessionId);
		if(userId==null){
			return "Invalid Token";
		}

		data.put("userId", userId);
		data.put("commandName", "RefundCommand");
		data.put("sessionId", sessionId);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		String random=UUID.randomUUID().toString();
		data.put("correlationId", random);
		try {
			jsonString = objectMapper.writeValueAsString( data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		 RequestReplyMessageFuture<String, Message<String>> result = replyingKafkaTemplate.sendAndReceive(MessageBuilder
                    .withPayload(jsonString)
                    .setHeader(KafkaHeaders.REPLY_TOPIC, "orderResponses")
                    .setHeader(KafkaHeaders.TOPIC, "payment")
                    .setHeader(KafkaHeaders.KEY, random)
                    .build());
		try{
			String payload = (String) result.get().getPayload();
			return orderFormulator.getData(payload);
		}catch(Exception e){
			return e.getMessage();
		}
	}

}
