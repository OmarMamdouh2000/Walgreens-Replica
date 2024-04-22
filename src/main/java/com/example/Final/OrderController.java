package com.example.Final;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public OrderRepo orderRepo;

	@Autowired
	public OrderService OrderService;

	@PostMapping("/addOrder")
	public void addOrder(@RequestBody OrderTable data) {
		System.out.println(data);
		orderRepo.save(data);
	}

	@GetMapping("/getOrders")
	public List<OrderTable> getUserOrders(@RequestBody Map<String,Object> data) {
		return OrderService.getUserOrders((String) data.get("token"));
	}

	@GetMapping("/filterOrders")
	public List<OrderTable> filterOrders(@RequestBody Map<String,Object> data){
		String dateString = (String) data.get("date");
		return OrderService.filterOrders((String) data.get("token"), (String) data.get("date"),(String) data.get("status"));
	}
}
