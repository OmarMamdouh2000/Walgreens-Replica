package com.example.Final;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import io.jsonwebtoken.Claims;

import org.springframework.beans.factory.annotation.Autowired;


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

	@GetMapping("/error")
	public String error() {
		return "error";
	}

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/getCart")
	public CartTable getCart(@RequestParam String token) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken(token, "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			throw new Exception("Invalid token");
		} else {
			try {
				return cartService.getUserCart((String) claims.get("userId"));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
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
	public CartTable removeItemFromCart(@RequestBody  Map<String, Object> data) throws Exception{
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		String item = (String) data.get("itemId");
		UUID itemID = UUID.fromString(item);

		if (claims == null) {
			throw new Exception("Invalid token");
		}
		else{
			try {
				return cartService.removeItemFromCart((String) claims.get("userId"), itemID);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}

	}

	@PostMapping("/changeOrderType")
	public void setOrderType(@RequestBody Map<String, Object> data) throws Exception {
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");

		if (claims == null) {
			throw new Exception("Invalid token");
		}
		else{
			try {
				cartService.setOrderType((String) claims.get("userId"), (String) data.get("orderType"), (String) data.get("itemId"));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}
	@PostMapping("/applyPromo")
	public CartTable applyPromo(@RequestBody Map<String, Object> data) throws Exception{
		Claims claims = jwtDecoderService.decodeJwtToken((String) data.get("token"), "ziad1234aaaa&&&&&thisisasecretekeyaaa");
		if (claims == null) {
			throw new Exception("Invalid token");
		}
		else{
			try {
				return cartService.applyPromo((String) claims.get("userId"), (String) data.get("promoId"));
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}
	@GetMapping("/getAllUsedPromo")
	public List<UserUsedPromo> getAllPromoUsed(){
		return cartService.getAllUsedPromo();
	}
	
}
