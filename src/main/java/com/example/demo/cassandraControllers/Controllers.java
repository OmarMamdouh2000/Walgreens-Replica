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
	
	@GetMapping("/listProducts")
	public Slice<Products> listProducts(@RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "10") int size) {
	    return service.listProductsService(page, size);
	}

	@GetMapping("/getProduct")
	public Products getProduct(@RequestBody Map<String, Object> body)
	{
		return service.getProductService(body);
	}
	
	@GetMapping("/listCategoryProducts")
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