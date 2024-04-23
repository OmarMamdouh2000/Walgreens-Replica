package com.example.demo.cassandraServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraRepositories.CategoriesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.cassandraModels.Brand;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.ProductSize;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;
import com.example.demo.cassandraRepositories.BrandRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;


@Service
public class Services {

	@Autowired
	private CategoriesRepo catRepo;
	@Autowired
	private ProductsRepo prodRepo;
	@Autowired
	private BrandRepo brandRepo;
	
	public List<Categories> listCategoriesService()
	{
		return catRepo.listCategoriesRepo();
	}
	
	public Categories getCategoryService(UUID categoryId)
	{
		return catRepo.getCategoryRepo(categoryId);
	}
	
	public void deleteCategoryService(UUID categoryId)
	{
		Categories category = catRepo.getCategoryRepo(categoryId);
		if(category.getParentCategory() != null)
		{
			Categories parentCategory = catRepo.getCategoryRepo(category.getParentCategory());
			parentCategory.getSubCategories().remove(categoryId);
			catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getProducts());
		}
		
		Stack<UUID> deletedStack = new Stack<>();
		deletedStack.push(categoryId);
		
		while(!deletedStack.isEmpty())
		{
			UUID deletedCategoryId = deletedStack.pop();
			Categories deletedCategory = catRepo.getCategoryRepo(deletedCategoryId);
			
			if(deletedCategory.getSubCategories() != null)
			{
				for(UUID subCategoryId: deletedCategory.getSubCategories())
				{
					deletedStack.push(subCategoryId);
				}
			}
			
			catRepo.deleteCategoryRepo(deletedCategoryId);
		}
	}
	
	public void addCategoryService(Map<String, Object> body)
	{
		UUID categoryId = Uuids.timeBased();
		String categoryName = null;
		String categoryImage = null;
		UUID parentCategoryId = null;
		List<UUID> categoryProducts = new ArrayList<>();
		List<UUID> categorySubCategories = new ArrayList<>();
		
		if(body.containsKey("name"))
			categoryName = (String)body.get("name");
		
		if(body.containsKey("image"))
			categoryImage = (String)body.get("image");
		
		if(body.containsKey("parentCategory"))
		{
			parentCategoryId = UUID.fromString((String)body.get("parentCategory"));
			Categories parentCategory = catRepo.getCategoryRepo(parentCategoryId);
			catRepo.addCategoryRepo(categoryId, categoryName, categoryImage, parentCategoryId, categoryProducts, categorySubCategories);
			
			if(parentCategory.getSubCategories() == null)
			{
				List<UUID> parentSubCategories = new ArrayList<>();
				parentSubCategories.add(categoryId);
				parentCategory.setSubCategories(parentSubCategories);
			}
			else
				parentCategory.getSubCategories().add(categoryId);
			
			catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getProducts());
		}
		else
			catRepo.addCategoryRepo(categoryId, categoryName, categoryImage, parentCategoryId, categoryProducts, categorySubCategories);
	}
	
	public void updateCategoryService(Map<String, Object> body)
	{
		String newCategoryName;
		String newCategoryImage;
		UUID parentCategoryId;
		List<UUID> newCategorySubCategories;
		List<UUID> newCategoryProducts;
		
		UUID categoryId = UUID.fromString((String)body.get("id"));
		Categories category = catRepo.getCategoryRepo(categoryId);
		
		if(body.containsKey("name"))
			newCategoryName = (String)body.get("name");
		else
			newCategoryName = category.getName();
		
		if(body.containsKey("image"))
			newCategoryImage = (String)body.get("image");
		else
			newCategoryImage = category.getImage();
		
		parentCategoryId = category.getParentCategory();
		newCategorySubCategories = category.getSubCategories();
		newCategoryProducts = category.getProducts();
		
		catRepo.updateCategoryRepo(categoryId, newCategoryName, newCategoryImage, parentCategoryId, newCategorySubCategories, newCategoryProducts);
	}


	

	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	public List<Products> listProductsService() {
	    if (prodRepo == null) {
	        throw new IllegalStateException("ProductsRepo not autowired correctly");
	    }
	    return prodRepo.listProductsRepo();
	}
	
	// Inside the Services class
	public Products getProductByIdService(UUID productId) {
	    return prodRepo.getProductRepo(productId);
	}

	public Slice<Products> getProducts(int page, int size) {
	    Slice<Products> productsSlice = prodRepo.findAll(CassandraPageRequest.of(0, size));
	    for (int i = 0; i < page; i++) {
	        if (productsSlice.hasNext()) {
	            productsSlice = prodRepo.findAll(productsSlice.nextPageable());
	        } else {
	            break;
	        }
	    }
	    return productsSlice;
	}
	
	 public void addProduct(String name, String description, int price, int discount, boolean sale, String image,
	            String productType, UUID brandId, ProductSize sizeList, String customMessage, boolean inStore,
	            boolean sameDayDelivery, boolean shipping, int inventory, int pricePerUnit, String ingredients,
	            String warnings, String frequentlyBoughtWith, boolean refundable) {
		 prodRepo.addProduct(UUID.randomUUID(), name, description, price, discount, sale, image, productType,
	                brandId, sizeList, customMessage, inStore, sameDayDelivery, shipping, inventory, pricePerUnit,
	                ingredients, warnings, frequentlyBoughtWith, refundable);
	    }
	 
	 public void updateProductService(Products product) {
		    prodRepo.updateProduct(product.getId(), product.getName(), product.getDescription(), product.getPrice(),
		            product.getDiscount(), product.isSale(), product.getImage(), product.getProductType(),
		            product.getBrandId(), product.getSizeList(), product.getCustomMessage(), product.isInStore(),
		            product.isSameDayDelivery(), product.isShipping(), product.getInventory(), product.getPricePerUnit(),
		            product.getIngredients(), product.getWarnings(), product.getFrequentlyBoughtWith(),
		            product.isRefundable());
	}
	 
	 public void deleteProductService(UUID productId) {
		    prodRepo.deleteProduct(productId);
	}

	
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	 
	 
	 
	// --------------------------------------------- BRANDS ------------------------------------------------
	 
	 public List<Brand> listBrandService(){
			return brandRepo.listBrandRepo();
		}
		
		public Brand getBrandService(UUID brandID) {
			return brandRepo.getBrandRepo(brandID);
		}
		
		public void deleteBrandService(UUID brandID)
		{
			brandRepo.deleteBrandRepo(brandID);
		}
		
		public void insertBrandService(Map<String, Object> body)
		{
			UUID brandId = Uuids.timeBased();
			String brandName = "";
			ArrayList<UUID> categories = new ArrayList<>();
			
			if(body.containsKey("name"))
				brandName = (String)body.get("name");
			
			brandRepo.insertBrandRepo(brandId,brandName,categories);
		}
		public void updateBrandService(Map<String, Object> body)
		{
			String newBrandName = "";

			
			UUID brandId = UUID.fromString((String)body.get("id"));
			Brand brand = brandRepo.getBrandRepo(brandId);
			
			if(body.containsKey("name"))
				newBrandName = (String)body.get("name");
			else
				newBrandName = brand.getName();
			
			
			brandRepo.updateBrandRepo(brandId, newBrandName);
		}
		
		// --------------------------------------------- END BRANDS ---------------------------------------------

}
