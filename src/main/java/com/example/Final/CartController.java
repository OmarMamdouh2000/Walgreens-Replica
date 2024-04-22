package com.example.Final;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import jakarta.annotation.PostConstruct;

@RestController
public class CartController {
	
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
	
	@Autowired
	public CartRepo cartRepo;
	
	@PostMapping("/editItemCount")
	public String editItemCount(@RequestBody Map<String,Object> data) {
		
		return service.updateItemCount((String)data.get("itemId"), (String)data.get("token"), (int)data.get("itemCount"));
		
		
	}
	@PostMapping("/addItemToSavedLater")
	public String addItemToSavedLater(@RequestBody Map<String, Object> data) {
		return service.addToSavedForLater((String)data.get("itemId"), (String)data.get("token"));
	}
	@PostMapping("/returnItemFromSavedLater")
	public String returnItemFromSavedLater(@RequestBody Map<String, Object> data) {
		return service.returnFromSavedForLater((String)data.get("itemId"), (String)data.get("token"));
	}
}
