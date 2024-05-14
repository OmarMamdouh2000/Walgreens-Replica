package com.example.demo.cassandraControllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.cassandraModels.Pobject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraCommands.Invoker;
import com.example.demo.cassandraKafka.KafkaProducer;
import com.example.demo.cassandraModels.Brands;


@RestController
public class Controllers {
	
	@Autowired
	private Invoker invoker;
	@Autowired
	private KafkaProducer kafkaProducerRequest;
	
	// --------------------------------------------- CATEGORIES ------------------------------------------------
	
	@GetMapping("/listCategories")
	public void listCategories()
	{
		//return (List<Categories>) invoker.executeCommand("listCategoriesCommand", null, null);
		
		Map<String,Object> body = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		body.put("commandName", "listCategories");
		
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
		//return (Categories) invoker.executeCommand("getCategoryCommand", categoryId, body);
		Map<String,Object> body = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		body.put("parameter", categoryId);
		body.put("commandName", "getCategory");
		
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@DeleteMapping("/deleteCategory/{categoryId}")
	public String deleteCategory(@PathVariable Object categoryId, @RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("deleteCategoryCommand", categoryId, body);
	}
	
	@PostMapping("/addCategory")
	public void addCategory(@RequestBody Map<String, Object> body)
	{
		//return (String) invoker.executeCommand("addCategoryCommand", null,  body);
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		body.put("commandName", "addCategory");
		
		try {
			jsonString = objectMapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			System.out.print(e.getMessage());
		}
		kafkaProducerRequest.publishToTopic("ProductsRequests",jsonString);
	}
	
	@PutMapping("/updateCategory/{categoryId}")
	public String updateCategory(@PathVariable Object categoryId, @RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("updateCategoryCommand", categoryId,  body);
	}
	
	// --------------------------------------------- END CATEGORIES ------------------------------------------------
	
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@GetMapping("/listProducts")
	public List<Products> listProducts(@RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "10") int size) {
			int [] params = new int [2];
			params[0] = page;
			params[1] = size; //(List<Products>)
	    return (List<Products>) invoker.executeCommand("listProductsCommand", params , null);
	}

	@GetMapping("/getProduct/{productId}")
	public Products getProduct(@PathVariable Object productId)
	{
		return (Products) invoker.executeCommand("getProductCommand", productId , null);
	}
	
	
	@SuppressWarnings("unchecked")
	@GetMapping("/listCategoryProducts/{categoryId}")
	public List<Pobject> listCategoryProducts(@PathVariable Object categoryId) { 
		return (List<Pobject>) invoker.executeCommand("listCategoryProductsCommand", categoryId , null);
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public String deleteProduct(@PathVariable Object productId) {
		return (String)invoker.executeCommand("deleteProductCommand", productId , null);
	}
	
	@PostMapping("/addProduct")
    public String addProduct(@RequestBody Map<String, Object> body) {
		return (String) invoker.executeCommand("addProductCommand", null , body);
   	}
	
	@PutMapping("/updateProduct/{productId}")
	public String updateProduct(@PathVariable Object productId, @RequestBody Map<String, Object> body) {
		return (String) invoker.executeCommand("updateProductCommand", productId , body);
	}
	
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	
	// --------------------------------------------- BRANDS ------------------------------------------------

	@SuppressWarnings("unchecked")
	@GetMapping("/listBrand")
	public List<Brands> ListBrand()
	{
		return (List<Brands>) invoker.executeCommand("listBrandsCommand", null, null);
	}
	
	@GetMapping("/getBrand/{brandId}")
	public Brands getBrand(@PathVariable Object brandId, @RequestBody Map<String, Object> body)
	{
		return (Brands) invoker.executeCommand("getBrandCommand", brandId, body);
	}
	
	@SuppressWarnings("unchecked")
	@GetMapping("/listBrandProducts/{brandId}")
	public List<Pobject> listBrandProducts(@PathVariable Object brandId, @RequestBody Map<String, Object> body) 
	{
	    return (List<Pobject>) invoker.executeCommand("listBrandProducts", brandId, body);
	}
	
	@DeleteMapping("/deleteBrand/{brandId}")
	public void deleteBrand(@PathVariable Object brandId, @RequestBody Map<String, Object> body)
	{
		 invoker.executeCommand("deleteBrandCommand", brandId, body);
	}
	@PostMapping("/addBrand")
	public void addBrand(@RequestBody Map<String, Object> body)
	{
		invoker.executeCommand("addBrandCommand", null, body);
	}
	
	@PutMapping("/updateBrand/{brandId}")
	public void updateBrand(@PathVariable Object brandId, @RequestBody Map<String, Object> body)
	{
		invoker.executeCommand("updateBrandCommand", brandId, body);
	}
	
	// --------------------------------------------- END BRANDS ---------------------------------------------
}