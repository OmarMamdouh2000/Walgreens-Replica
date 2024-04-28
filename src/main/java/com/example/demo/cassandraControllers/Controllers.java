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
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraServices.Services;



import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraModels.Brand;


@RestController
public class Controllers {
	
	@Autowired
	private Services service;
	
	// --------------------------------------------- CATEGORIES ------------------------------------------------
	
	@GetMapping("/listCategories")
	public List<Categories> listCategories()
	{
		return service.listCategoriesService();
	}
	
	@GetMapping("/getCategory/{categoryId}")
	public Categories getCategory(@PathVariable UUID categoryId)
	{
		return service.getCategoryService(categoryId);
	}
	
	@DeleteMapping("/deleteCategory/{categoryId}")
	public void deleteCategory(@PathVariable UUID categoryId)
	{
		service.deleteCategoryService(categoryId);
	}
	
	@PostMapping("/addCategory")
	public void addCategory(@RequestBody Map<String, Object> body)
	{
		service.addCategoryService(body);
	}
	
	@PostMapping("/addCategory/{categoryId}")
	public void addCategory(@PathVariable UUID categoryId, @RequestBody Map<String, Object> body)
	{
		service.addCategoryService(categoryId, body);
	}
	
	@PutMapping("/updateCategory/{categoryId}")
	public void updateCategory(@PathVariable UUID categoryId, @RequestBody Map<String, Object> body)
	{
		service.updateCategoryService(categoryId, body);
	}
	
	// --------------------------------------------- END CATEGORIES ------------------------------------------------
	
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
//	@GetMapping("/getAllProducts")
//	public List<Products> listProducts() {
//	    return service.listProductsService();
//	}
	@GetMapping("/listProducts")
	public Slice<Products> listProducts(@RequestParam(name = "page", defaultValue = "0") int page,
	        @RequestParam(name = "size", defaultValue = "10") int size) {
	    return service.listProductsService(page, size);
	}

	@GetMapping("/getProduct/{productId}")
	public Products getProduct(@PathVariable UUID productId) {
	    return service.getProductService(productId);
	}
	
	@GetMapping("/listCategoryProducts/{categoryId}")
	public List<Pobject> listCategoryProducts(@PathVariable UUID categoryId) {
	    return service.listCategoryProductsService(categoryId);
	}
	
	@DeleteMapping("/deleteProduct/{productId}")
	public void deleteProduct(@PathVariable UUID productId) {
	    service.deleteProductService(productId);
	}
	
	@PostMapping("/addProduct")
    public void addProduct(@RequestBody Map<String, Object> body) {
        service.addProductService(body);
   	}
	
	@PostMapping("/addProduct/{categoryId}")
    public void addProduct(@PathVariable UUID categoryId, @RequestBody Map<String, Object> body) {
        service.addProductService(categoryId, body);
   	}
	
	@PutMapping("/updateProduct/{productId}")
	public void updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
	    service.updateProductService(productId, body);
	}
	
//	@PostMapping("/products")
//    public void addProduct(@RequestBody Products product) {
//        service.addProduct(product.getName(), product.getDescription(), product.getPrice(),
//                product.getDiscount(), product.isSale(), product.getImage(), product.getProductType(),
//                product.getBrandId(), product.getSizeList(), product.getCustomMessage(), product.isInStore(),
//                product.isSameDayDelivery(), product.isShipping(), product.getInventory(), product.getPricePerUnit(),
//                product.getIngredients(), product.getWarnings(), product.getFrequentlyBoughtWith(),
//                product.isRefundable());
//   	}
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	
	// --------------------------------------------- BRANDS ------------------------------------------------

	@GetMapping("/listBrand")
	public List<Brand> ListBrand()
	{
		return service.listBrandService();
	}
	
	@GetMapping("/getBrand/{brandId}")
	public Brand getBrand(@PathVariable UUID brandId) 
	{
		return service.getBrandService(brandId);
	}
	
	@GetMapping("/listBrandProducts/{brandId}")
	public List<Pobject> listBrandProducts(@PathVariable UUID brandId) {
	    return service.listBrandProductsService(brandId);
	}
	
	@DeleteMapping("/deleteBrand/{brandId}")
	public void deleteBrand(@PathVariable UUID brandId)
	{
		service.deleteBrandService(brandId);
	}
	@PostMapping("/addBrand")
	public void addBrand(@RequestBody Map<String, Object> body)
	{
		service.addBrandService(body);
	}
	
	@PutMapping("/updateBrand/{brandId}")
	public void updateBrand(@PathVariable UUID brandId, @RequestBody Map<String, Object> body)
	{
		service.updateBrandService(brandId, body);
	}
	
	// --------------------------------------------- END BRANDS ---------------------------------------------
}