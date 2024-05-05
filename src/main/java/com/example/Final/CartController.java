package com.example.Final;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import io.jsonwebtoken.Claims;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;

import com.example.Commands.Invoker;
import com.example.Commands.JwtDecoderService;
import java.time.Duration;
import org.springframework.web.bind.annotation.*;
import com.example.Kafka.KafkaProducer;

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
		// JSONObject jsonObject = new JSONObject();
		// jsonObject.put("commandName", "UpdateItemCountCommand");
		// for(String key : data.keySet()) {
		// 	jsonObject.put(key, data.get(key));
		// }
		// String jsonString = jsonObject.toString();
		// // ProducerRecord<String, String> record = new ProducerRecord<>("cartRequests", jsonString);
		// // RequestReplyFuture<String, String, String> future = replyingKafkaTemplate.sendAndReceive(record);
		// // System.out.println();
		// // // Get the reply
		// // ConsumerRecord<String, String> reply;
		// // try {
		// // 	reply = future.get();
		// // } catch (InterruptedException | ExecutionException e) {
		// // 	e.printStackTrace();
		// // 	throw new RuntimeException("Error processing request", e);
		// // }

		// // return reply.value();
		// //kafkaProducerResponse.publishToTopic("cartResponses",jsonString);
		// kafkaProducerRequest.publishToTopic("cartRequests2",jsonString);

		// return "success";
		data.put("token", token);
		return invoker.executeCommand("UpdateItemCountCommand", data).toString();
		
	}
	@PostMapping("/addItemToSavedLater")
	public String addItemToSavedLater(@RequestParam String token,@RequestBody Map<String, Object> data) {
		data.put("token", token);
		return invoker.executeCommand("AddToSavedForLater", data).toString();
	}
	@PostMapping("/returnItemFromSavedLater")
	public String returnItemFromSavedLater(@RequestParam String token,@RequestBody Map<String, Object> data) {
		data.put("token", token);
		return invoker.executeCommand("ReturnFromSavedForLater", data).toString();
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
