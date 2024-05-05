package com.example.Final;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.Claims;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


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

	// @Autowired
	// private ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate;
	@Autowired
	KafkaProducer kafkaProducerResponse;
	@Autowired
	KafkaProducer kafkaProducerRequest;

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/getCart")
	public Object getCart(@RequestParam String token) {
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
		} else {
			return invoker.executeCommand("GetUserCart", Map.of("User", claims.get("userId")));
		}
	}

	@PostMapping("/editItemCount")
	public String editItemCount(@RequestParam String token,@RequestBody Map<String,Object> data) {


		data.put("token", token);
		data.put("commandName", "UpdateItemCountCommand");
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
	public String addItemToSavedLater(@RequestParam String token,@RequestBody Map<String, Object> data) {
		data.put("token", token);
		data.put("commandName", "AddToSavedForLater");
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
	public String returnItemFromSavedLater(@RequestParam String token,@RequestBody Map<String, Object> data) {
		data.put("token", token);
		data.put("commandName", "ReturnFromSavedForLater");
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
	public Object removeItemFromCart(@RequestParam String token ,@RequestBody Map<String, Object> data) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");

		if (claims == null) return new ResponseEntity<>("Invalid Token", HttpStatus.UNAUTHORIZED);
		else {
			return invoker.executeCommand("RemoveItem", Map.of("User", claims.get("userId"), "Item", data.get("itemId")));
		}
	}

	@PostMapping("/changeOrderType")
	public Object setOrderType(@RequestParam String token, @RequestBody Map<String, Object> data) throws Exception{
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");

		if (claims == null) return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
		else{
			return invoker.executeCommand("ChangeOrderType", Map.of("User", claims.get("userId"), "Data", data));
		}
	}

	@PostMapping("/applyPromo")
	public Object applyPromo(@RequestParam String token, @RequestBody Map<String, Object> data){
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");

		if (claims == null) return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
		else{
			return invoker.executeCommand("ApplyPromo", Map.of("User", claims.get("userId"), "Data", data));
		}
	}

	@GetMapping("/getAllUsedPromo")
	public List<UserUsedPromo> getAllPromoUsed(){
		return cartService.getAllUsedPromo();
	}

}
