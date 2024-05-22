package com.example.Kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.Commands.Invoker;
import com.example.Final.OrderTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Service
public class KafkaConsumerRequests {
	@Autowired
	Invoker invoker=new Invoker();
	@Autowired
	KafkaProducer kafkaProducer;
	@Autowired
	private ReplyingKafkaTemplate<String, Message<String>, Message<String>> replyingKafkaTemplate;
	Logger logger = LoggerFactory.getLogger(KafkaConsumerRequests.class);
	@KafkaListener(topics="orderRequests",groupId = "KafkaGroupRequestOrder")
	public void consumeMessage(@Payload String message, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationIdBytes, @Header(KafkaHeaders.REPLY_TOPIC) String replyTopic) {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			message=message.replace("\\", "");
			message=message.substring(1, message.length()-1);
			logger.info("Request: "+message);
			@SuppressWarnings("unchecked")
			Map<String ,Object>data = objectMapper.readValue(message, HashMap.class);
			objectMapper.registerModule(new JavaTimeModule());
			Object finalData =null;
			Map<String,Object> result=new HashMap<>();
			result.put("commandName",data.get("commandName").toString());


        switch (data.get("commandName").toString()) {
			case "CreateOrder":
				finalData = (String) invoker.executeCommand("CreateOrder", data);

				break;
            case "GetOrdersCommand":
				finalData = invoker.executeCommand("GetOrdersCommand", data);

                break;
			case "GetActiveOrdersCommand":
				finalData = invoker.executeCommand("GetActiveOrdersCommand", data);

				break;

			case "FilterOrders":
				finalData= invoker.executeCommand("FilterOrders", data);

				break;
			case "ConfirmRefund":
				finalData = (String)invoker.executeCommand("ConfirmRefund", data);

				break;
            default:
                break;
        }
		result.put("data",finalData);
		String response = objectMapper.writeValueAsString(result);
		kafkaProducer.publishToTopic("orderResponses", response,correlationIdBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
