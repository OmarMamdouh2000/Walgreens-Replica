package com.example.Kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyMessageFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.Commands.Invoker;
import com.example.Final.CartTable;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class KafkaConsumerRequests {
	@Autowired
	Invoker invoker=new Invoker();
	@Autowired
	KafkaProducer kafkaProducer;
	@Autowired
	private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;
	
	Logger logger = LoggerFactory.getLogger(KafkaConsumerRequests.class);
	@KafkaListener(topics="cartRequests",groupId = "KafkaGroupRequestCart")
	public void consumeMessage(@Payload String message, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationIdBytes, @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic) {
		try {
			
			logger.info("correlationid recieved: "+Arrays.toString(correlationIdBytes));
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			logger.info("Request: "+message);
			//[99, -17, -95, 46, -54, 23, 68, -64, -74, 87, 98, 62, 15, 22, 3, 26]
			ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String,Object> data = objectMapper.readValue(message, HashMap.class);
			Map<String,Object> result = new HashMap<>();
			result.put("commandName", data.get("commandName").toString());
			Object finalData="";
			String jsonString2="";
			switch (data.get("commandName").toString()) {
				
				case "UpdateItemCountCommandCache":
					finalData = (String) invoker.executeCommand("UpdateItemCountCommandCache", data);
					data.replace("commandName", "UpdateItemCountCommand");
					jsonString2=objectMapper.writeValueAsString(data);
					replyingKafkaTemplate.sendAndReceive(MessageBuilder
							.withPayload(jsonString2)
							.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
							.setHeader(KafkaHeaders.TOPIC, "cartRequests")
							.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
							.build());					break;

				case "UpdateItemCountCommand":
					finalData= (String) invoker.executeCommand("UpdateItemCountCommand", data);
					break;

				case "AddToSavedForLaterCache":
					finalData = (String) invoker.executeCommand("AddToSavedForLaterCache", data);
					data.replace("commandName", "AddToSavedForLater");
					jsonString2=objectMapper.writeValueAsString(data);
					replyingKafkaTemplate.sendAndReceive(MessageBuilder
							.withPayload(jsonString2)
							.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
							.setHeader(KafkaHeaders.TOPIC, "cartRequests")
							.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
							.build());
					break;
				case "AddToSavedForLater":
					finalData= (String) invoker.executeCommand("AddToSavedForLater", data);
					break;

				case "ReturnFromSavedForLaterCache":
					finalData = (String) invoker.executeCommand("ReturnFromSavedForLaterCache", data);
					data.replace("commandName", "ReturnFromSavedForLater");
					jsonString2=objectMapper.writeValueAsString(data);
					replyingKafkaTemplate.sendAndReceive(MessageBuilder
							.withPayload(jsonString2)
							.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
							.setHeader(KafkaHeaders.TOPIC, "cartRequests")
							.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
							.build());
					break;
				case "ReturnFromSavedForLater":
					finalData= (String) invoker.executeCommand("ReturnFromSavedForLater", data);
					break;
				case "GetUserCart":
					finalData = (Object) invoker.executeCommand("GetUserCart", data);
					break;
				case "RemoveItem":
					finalData = (Object) invoker.executeCommand("RemoveItem", data);
					try{
						CartTable cart = (CartTable) finalData;
						data.replace("commandName", "UpdateCart");
						data.put("cart", finalData);
						jsonString2=objectMapper.writeValueAsString(data);
						replyingKafkaTemplate.sendAndReceive(MessageBuilder
								.withPayload(jsonString2)
								.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
								.setHeader(KafkaHeaders.TOPIC, "cartRequests")
								.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
								.build());
					}catch(Exception e){
						System.out.println("Item wasn't removed");
						logger.error("Item wasn't removed");
					}
					break;
				case "ChangeOrderType":
					finalData = (Object) invoker.executeCommand("ChangeOrderType", data);
					try{
						CartTable cart = (CartTable) finalData;
						data.replace("commandName", "UpdateCart");
						data.put("cart", finalData);
						jsonString2=objectMapper.writeValueAsString(data);
						replyingKafkaTemplate.sendAndReceive(MessageBuilder
								.withPayload(jsonString2)
								.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
								.setHeader(KafkaHeaders.TOPIC, "cartRequests")
								.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
//								.setHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID())
								.build());
//						kafkaProducer.publishToTopic("cartRequests", jsonString2,correlationIdBytes);
					}catch(Exception e){
						System.out.println("Cart wasn't changed");
						logger.error("Cart wasn't changed");
					}
					break;
				case "ApplyPromo":
					finalData = (Object) invoker.executeCommand("ApplyPromo", data);
					try{
						CartTable cart = (CartTable) finalData;
						data.replace("commandName", "UpdateCart");
						data.put("cart", finalData);
						jsonString2=objectMapper.writeValueAsString(data);
						replyingKafkaTemplate.sendAndReceive(MessageBuilder
								.withPayload(jsonString2)
								.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
								.setHeader(KafkaHeaders.TOPIC, "cartRequests")
								.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
								.build());
					}catch(Exception e){
						System.out.println("Promo Not Applied");
						logger.error("Promo Not Applied");
					}

					break;
				case "AddItem":
					finalData = (Object) invoker.executeCommand("AddItem", data);
					try{
						CartTable cart = (CartTable) finalData;
						data.replace("commandName", "UpdateCart");
						data.put("cart", finalData);
						jsonString2=objectMapper.writeValueAsString(data);
						replyingKafkaTemplate.sendAndReceive(MessageBuilder
								.withPayload(jsonString2)
								.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
								.setHeader(KafkaHeaders.TOPIC, "cartRequests")
								.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
								.build());
					}catch(Exception e){
						System.out.println("Item wasn't added");
						logger.error("Item wasn't added");
					}
					break;
				case "AddComment":
					finalData = (Object) invoker.executeCommand("AddComment", data);
					try{
						CartTable cart = (CartTable) finalData;
						data.replace("commandName", "UpdateCart");
						data.put("cart", finalData);
						jsonString2=objectMapper.writeValueAsString(data);
						replyingKafkaTemplate.sendAndReceive(MessageBuilder
								.withPayload(jsonString2)
								.setHeader(KafkaHeaders.REPLY_TOPIC, "cartResponses")
								.setHeader(KafkaHeaders.TOPIC, "cartRequests")
								.setHeader(KafkaHeaders.KEY, UUID.randomUUID().toString())
								.build());
					}catch(Exception e){
						System.out.println("Comment wasn't added");
						logger.error("Comment wasn't added");
					}
					break;
			
				case "ProceedToCheckOutCommand":
					finalData = (Object) invoker.executeCommand("ProceedToCheckOutCommand", data);
					break;
				case "ConfirmCheckoutCommand":
					finalData = (Object) invoker.executeCommand("ConfirmCheckoutCommand", data);
					break;
				case "UpdateCart":
					finalData = (Object) invoker.executeCommand("UpdateCart", data);
					break;
				case "GetProductForCartCommand":
					finalData = (Object) invoker.executeCommand("AddItem", data);
					break;
				default:
					break;
			}
			result.put("data", finalData);
			result.put("correlationId", data.get("correlationId"));
			String response = objectMapper.writeValueAsString(result);
			kafkaProducer.publishToTopic("cartResponses", response,correlationIdBytes);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
