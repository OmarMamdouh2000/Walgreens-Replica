package com.example.demo.cassandraControllers;

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

import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraServices.Services;



import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraCommands.Invoker;
import com.example.demo.cassandraModels.Brands;


@RestController
public class Controllers {
	
	@Autowired
	private Services service;
	@Autowired
	private Invoker invoker;
	
	// --------------------------------------------- CATEGORIES ------------------------------------------------
	
	@SuppressWarnings("unchecked")
	@GetMapping("/listCategories")
	public List<Categories> listCategories()
	{
		return (List<Categories>) invoker.executeCommand("listCategoriesCommand", null, null);
	}
	
	@GetMapping("/getCategory/{categoryId}")
	public Categories getCategory(@PathVariable Object categoryId, @RequestBody Map<String, Object> body)
	{
		return (Categories) invoker.executeCommand("getCategoryCommand", categoryId, body);
	}
	
	@DeleteMapping("/deleteCategory/{categoryId}")
	public String deleteCategory(@PathVariable Object categoryId, @RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("deleteCategoryCommand", categoryId, body);
	}
	
	@PostMapping("/addCategory")
	public String addCategory(@RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("addCategoryCommand", null,  body);
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

	@GetMapping("/listBrand")
	public List<Brands> ListBrand()
	{
		return service.listBrandService();
	}
	
	@GetMapping("/getBrand")
	public Brands getBrand(@RequestBody Map<String, Object> body)
	{
		return service.getBrandService(body);
	}
	
	@GetMapping("/listBrandProducts")
	public List<Pobject> listBrandProducts(@RequestBody Map<String, Object> body) 
	{
	    return service.listBrandProductsService(body);
	}
	
	@DeleteMapping("/deleteBrand")
	public void deleteBrand(@RequestBody Map<String, Object> body)
	{
		service.deleteBrandService(body);
	}
	@PostMapping("/addBrand")
	public void addBrand(@RequestBody Map<String, Object> body)
	{
		service.addBrandService(body);
	}
	
	@PutMapping("/updateBrand")
	public void updateBrand(@RequestBody Map<String, Object> body)
	{
		service.updateBrandService(body);
	}
	
	// --------------------------------------------- END BRANDS ---------------------------------------------
}