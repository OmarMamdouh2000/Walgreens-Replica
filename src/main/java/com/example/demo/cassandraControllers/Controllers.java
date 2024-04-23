package com.example.demo.cassandraControllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraServices.Services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraModels.Brand;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraServices.Services;


@RestController
public class Controllers {
	
	@Autowired
	private Services service;
	
	@GetMapping("/listCategories")
	public List<Categories> listCategories()
	{
		return service.listCategoriesService();
	}
	
	@GetMapping("/getCategory")
	public Categories getCategory(@RequestBody Map<String, UUID> body)
	{
		return service.getCategoryService(body.get("id"));
	}
	
	@DeleteMapping("/deleteCategory")
	public void deleteCategory(@RequestBody Map<String, UUID> body)
	{
		service.deleteCategoryService(body.get("id"));
	}
	
	@PostMapping("/addCategory")
	public void addCategory(@RequestBody Map<String, Object> body)
	{
		service.addCategoryService(body);
	}
	
	@PutMapping("/updateCategory")
	public void updateCategory(@RequestBody Map<String, Object> body)
	{
		service.updateCategoryService(body);
	}
	
//	@GetMapping("/getAllCategoryNames")
//	public List<String> findAllCategoryNames()
//	{
//		List<Categories> allCategories = catRepo.findAll();
//		List<String> categoryNames = new ArrayList<>();
//		for(Categories category: allCategories)
//		{
//			categoryNames.add(category.getName());
//		}
//		return categoryNames;
//	}
	
//	
//	public ResponseEntity<Categories> addCategoryCassandra(@RequestBody Categories category)
//	{
//		Categories savedCategory = catRepo.save(category);
//		return ResponseEntity.ok(savedCategory);
//	}



	
	
	
	
	
	
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	@GetMapping("/getAllProducts")
	public List<Products> listProducts() {
	    return service.listProductsService();
	}
	@GetMapping("/products")
	public Slice<Products> getProducts(@RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "10") int size) {
	    return service.getProducts(page, size);
	}

	@GetMapping("/getProduct/{productId}")
	public Products getProductById(@PathVariable UUID productId) {
	    return service.getProductByIdService(productId);
	}
	
	@PostMapping("/products")
    public void addProduct(@RequestBody Products product) {
        service.addProduct(product.getName(), product.getDescription(), product.getPrice(),
                product.getDiscount(), product.isSale(), product.getImage(), product.getProductType(),
                product.getBrandId(), product.getSizeList(), product.getCustomMessage(), product.isInStore(),
                product.isSameDayDelivery(), product.isShipping(), product.getInventory(), product.getPricePerUnit(),
                product.getIngredients(), product.getWarnings(), product.getFrequentlyBoughtWith(),
                product.isRefundable());
   	}
	
	@PutMapping("/updateProduct")
	public void updateProduct(@RequestBody Products product) {
	    service.updateProductService(product);
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public void deleteProduct(@PathVariable UUID productId) {
	    service.deleteProductService(productId);
	}
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	
	// --------------------------------------------- BRANDS ------------------------------------------------

	@GetMapping("/getAllBrand")
	public List<Brand> ListBrand()
	{
		return service.listBrandService();
	}
	
	@GetMapping("/getBrand")
	public Brand getBrand(@RequestBody Map<String, UUID> data) {
		return service.getBrandService(data.get("id"));
	}
	
	@DeleteMapping("/deleteBrand")
	public void deleteBrand(@RequestBody Map<String, UUID> body)
	{
		service.deleteBrandService(body.get("id"));
	}
	@PostMapping("/insertBrand")
	public void insertBrand(@RequestBody Map<String, Object> body)
	{
		service.insertBrandService(body);
	}
	
	@PutMapping("/updateBrand")
	public void updateBrand(@RequestBody Map<String, Object> body)
	{
		service.updateBrandService(body);
	}
	
	// --------------------------------------------- END BRANDS ---------------------------------------------


	
//	@GetMapping("/getAllCategoryNames")
//	public List<String> findAllCategoryNames()
//	{
//		List<Categories> allCategories = catRepo.findAll();
//		List<String> categoryNames = new ArrayList<>();
//		for(Categories category: allCategories)
//		{
//			categoryNames.add(category.getName());
//		}
//		return categoryNames;
//	}
	
//	
//	public ResponseEntity<Categories> addCategoryCassandra(@RequestBody Categories category)
//	{
//		Categories savedCategory = catRepo.save(category);
//		return ResponseEntity.ok(savedCategory);
//	}
}