package com.example.Final;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Commands.Invoker;
import com.example.Kafka.KafkaProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@RestController
public class OrderController {
	
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
	public OrderService OrderService;

	@Autowired
	public KafkaProducer kafkaProducer;

	@PostMapping("/addOrder")
	public void addOrder(@RequestBody OrderTable data) {
		System.out.println(data);
		orderRepo.save(data);
	}

	
	@PostMapping("/filterOrders")
	public Object filterOrders(@RequestParam String token, @RequestBody Map<String,Object> data) {

		data.put("token", token);
		data.put("commandName", "FilterOrders");
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		kafkaProducer.publishToTopic("orderRequests",jsonString);

		return "success";
//		String dateString = (String) data.get("date");
//		return OrderService.filterOrders((String) data.get("token"), (String) data.get("date"), (String) data.get("status"));
	}
	
	@GetMapping("/getOrders")
	public List<OrderTable> getOrders(@RequestParam String token) {
		
		Map<String,Object> data = new HashMap<String, Object>();
		
		data.put("token", token);
		data.put("commandName", "GetOrdersCommand");
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		kafkaProducer.publishToTopic("orderRequests", jsonString);


		return new ArrayList<OrderTable>();
		//return (List<OrderTable>) invoker.executeCommand("GetOrdersCommand", data);
		
		
	}
	@GetMapping("/getActiveOrders")
	public List<OrderTable> getActiveOrders(@RequestParam String token) {
		Map<String,Object> data = new HashMap<String, Object>();

		data.put("token", token);
		data.put("commandName", "GetActiveOrdersCommand");
		ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
		kafkaProducer.publishToTopic("orderRequests", jsonString);


		return new ArrayList<OrderTable>();
		//return (List<OrderTable>) invoker.executeCommand("GetActiveOrdersCommand", data);
		
		
	}
//	@GetMapping("/getOrderByDate")
//	public OrderTable getOrderByDate(@RequestParam String date) {
//		System.out.println(date+"-----------------");
//		return service.getOrderByDate(date);
//	}
}
