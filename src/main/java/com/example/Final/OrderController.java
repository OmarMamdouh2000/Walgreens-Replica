package com.example.Final;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
	public Services service;
	@GetMapping("/getOrders")
	public List<OrderTable> getOrders(@RequestBody Map<String,Object> data) {
		
		return service.getOrders((String) data.get("token"));
		
		
	}
	@GetMapping("/getActiveOrders")
	public List<OrderTable> getActiveOrders(@RequestBody Map<String,Object> data) {
		
		return service.getActiveOrders((String) data.get("token"));
		
		
	}
	@GetMapping("/getOrderByDate")
	public OrderTable getOrderByDate(@RequestParam String date) {
		System.out.println(date+"-----------------");
		return service.getOrderByDate(date);
	}
}
