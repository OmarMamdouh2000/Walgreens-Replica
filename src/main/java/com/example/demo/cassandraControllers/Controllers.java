package com.example.demo.cassandraControllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.cassandraKafka.KafkaProducer;


@RestController
public class Controllers {
	
	@Autowired
	private KafkaProducer kafkaProducerRequest;
	
	// --------------------------------------------- CATEGORIES ------------------------------------------------
	
	@GetMapping("/listCategories")
	public void listCategories()
	{	
		Map<String,Object> body = new HashMap<>();
		body.put("commandName", "listCategoriesCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	
	@GetMapping("/getCategory/{categoryId}")
	public void getCategory(@PathVariable Object categoryId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", categoryId);
		body.put("commandName", "getCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@GetMapping("/listCategoryProducts/{categoryId}")
	public void listCategoryProducts(@PathVariable Object categoryId) 
	{ 
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", categoryId);
		body.put("commandName", "listCategoryProductsCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@DeleteMapping("/deleteCategory/{categoryId}")
	public void deleteCategory(@PathVariable Object categoryId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", categoryId);
		body.put("commandName", "deleteCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@PostMapping("/addCategory")
	public void addCategory(@RequestBody Map<String, Object> body)
	{
		body.put("commandName", "addCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@PutMapping("/updateCategory/{categoryId}")
	public void updateCategory(@PathVariable Object categoryId, @RequestBody Map<String, Object> body)
	{
		body.put("parameter", categoryId);
		body.put("commandName", "updateCategoryCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	// --------------------------------------------- END CATEGORIES ------------------------------------------------
	
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	@GetMapping("/listProducts")
	public void listProducts(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
			int [] params = new int [2];
			params[0] = page;
			params[1] = size; //(List<Products>)
			
			Map<String,Object> body = new HashMap<>();
			body.put("parameter", params);
			body.put("commandName", "listProductsCase");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonString = null;
			try {
				jsonString = objectMapper.writeValueAsString(body);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				System.out.print(e.getMessage());
			}
			kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}

	@GetMapping("/getProduct/{productId}")
	public void getProduct(@PathVariable Object productId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", productId);
		body.put("commandName", "getProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public void deleteProduct(@PathVariable Object productId) 
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", productId);
		body.put("commandName", "deleteProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@PostMapping("/addProduct")
    public void addProduct(@RequestBody Map<String, Object> body) 
	{
		body.put("commandName", "addProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
   	}
	
	@PutMapping("/updateProduct/{productId}")
	public void updateProduct(@PathVariable Object productId, @RequestBody Map<String, Object> body) 
	{
		body.put("parameter", productId);
		body.put("commandName", "updateProductCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	
	// --------------------------------------------- BRANDS ------------------------------------------------

	@GetMapping("/listBrand")
	public void ListBrand()
	{
		Map<String,Object> body = new HashMap<>();
		body.put("commandName", "listBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	
	@GetMapping("/getBrand/{brandId}")
	public void getBrand(@PathVariable Object brandId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", brandId);
		body.put("commandName", "getBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	
	@GetMapping("/listBrandProducts/{brandId}")
	public void listBrandProducts(@PathVariable Object brandId) 
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", brandId);
		body.put("commandName", "listBrandProductsCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	
	@DeleteMapping("/deleteBrand/{brandId}")
	public void deleteBrand(@PathVariable Object brandId)
	{
		Map<String,Object> body = new HashMap<>();
		body.put("parameter", brandId);
		body.put("commandName", "deleteBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	@PostMapping("/addBrand")
	public void addBrand(@RequestBody Map<String, Object> body)
	{
		body.put("commandName", "addBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	
	@PutMapping("/updateBrand/{brandId}")
	public void updateBrand(@PathVariable Object brandId, @RequestBody Map<String, Object> body)
	{
		body.put("parameter", brandId);
		body.put("commandName", "updateBrandCase");
		
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		
		kafkaProducerRequest.publishToTopic("ProductsRequests", jsonString);
	}
	
	// --------------------------------------------- END BRANDS ---------------------------------------------
}