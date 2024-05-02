package com.example.demo.cassandraControllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
		return (List<Categories>) invoker.executeCommand("listCategoriesCommand", null);
	}
	
	@PostMapping("/getCategory")
	public Categories getCategory(@RequestBody Map<String, Object> body)
	{
		return (Categories) invoker.executeCommand("getCategoryCommand", body);
	}
	
	@DeleteMapping("/deleteCategory")
	public String deleteCategory(@RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("deleteCategoryCommand", body);
	}
	
	@PostMapping("/addCategory")
	public String addCategory(@RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("addCategoryCommand", body);
	}
	
	@PutMapping("/updateCategory")
	public String updateCategory(@RequestBody Map<String, Object> body)
	{
		return (String) invoker.executeCommand("updateCategoryCommand", body);
	}
	
	// --------------------------------------------- END CATEGORIES ------------------------------------------------
	
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	@GetMapping("/listProducts")
	public Slice<Products> listProducts(@RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "10") int size) {
	    return service.listProductsService(page, size);
	}

	@PostMapping("/getProduct")
	public Products getProduct(@RequestBody Map<String, Object> body)
	{
		return service.getProductService(body);
	}
	
	@PostMapping("/listCategoryProducts")
	public List<Pobject> listCategoryProducts(@RequestBody Map<String, Object> body) {
	    return service.listCategoryProductsService(body);
	}
	
	@DeleteMapping("/deleteProduct")
	public void deleteProduct(@RequestBody Map<String, Object> body) {
	    service.deleteProductService(body);
	}
	
	@PostMapping("/addProduct")
    public void addProduct(@RequestBody Map<String, Object> body) {
        service.addProductService(body);
   	}
	
	@PutMapping("/updateProduct")
	public void updateProduct(@RequestBody Map<String, Object> body) {
	    service.updateProductService(body);
	}
	
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	
	// --------------------------------------------- BRANDS ------------------------------------------------

	@GetMapping("/listBrand")
	public List<Brands> ListBrand()
	{
		return service.listBrandService();
	}
	
	@PostMapping("/getBrand")
	public Brands getBrand(@RequestBody Map<String, Object> body)
	{
		return service.getBrandService(body);
	}
	
	@PostMapping("/listBrandProducts")
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