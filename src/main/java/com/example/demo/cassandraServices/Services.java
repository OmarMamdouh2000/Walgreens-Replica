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


import org.springframework.data.cassandra.core.query.CassandraPageRequest;

import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraRepositories.ProductsRepo;

import com.example.demo.cassandraRepositories.BrandsRepo;
import org.springframework.data.domain.Slice;


@Service
public class Services {

	@Autowired
	private CategoriesRepo catRepo;
	@Autowired
	private ProductsRepo prodRepo;
	@Autowired
	private BrandsRepo brandRepo;
	
	// --------------------------------------------- CATEGORIES ---------------------------------------------
	
//	public List<Categories> listCategoriesService()
//	{
//		return catRepo.listCategoriesRepo();
//	}
	
//	public Categories getCategoryService(Map<String, Object> body)
//	{
//		UUID categoryId = UUID.fromString((String)body.get("id"));
//		Categories returnedCategory = catRepo.getCategoryRepo(categoryId);
//		return returnedCategory;
//	}
	
//	public void deleteCategoryService(Map<String, Object> body)
//	{
//		UUID categoryId = UUID.fromString((String)body.get("id"));
//		Categories category = catRepo.getCategoryRepo(categoryId);
//		
//		//Set all products to be null parent category if had products
//		if(category.getCategoryProducts() != null)
//		{
//			for(Pobject prod: category.getCategoryProducts())
//			{
//				Products product = prodRepo.getProductRepo(prod.getId());
//				product.setParentCategory(null);
//				prodRepo.updateProductRepo(product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getDiscount(), product.getDescription(), product.getBrand(), product.getParentCategory());
//			}
//		}
//		
//		if(category.getSubCategories() != null)
//		{
//			if(category.getParentCategory() != null)
//			{
//				Categories parentCategory = catRepo.getCategoryRepo(category.getParentCategory());
//				for(UUID subCategoryId: category.getSubCategories())
//				{
//					Categories subCategory = catRepo.getCategoryRepo(subCategoryId);
//					subCategory.setParentCategory(parentCategory.getId());
//					catRepo.updateCategoryRepo(subCategory.getId(), subCategory.getName(), subCategory.getImage(), subCategory.getParentCategory(), subCategory.getSubCategories(), subCategory.getCategoryProducts());
//					
//					parentCategory.getSubCategories().add(subCategoryId);
//				}
//				
//				parentCategory.getSubCategories().remove(categoryId);
//				catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
//			}
//			else
//			{
//				for(UUID subCategoryId: category.getSubCategories())
//				{
//					Categories subCategory = catRepo.getCategoryRepo(subCategoryId);
//					subCategory.setParentCategory(null);
//					catRepo.updateCategoryRepo(subCategory.getId(), subCategory.getName(), subCategory.getImage(), subCategory.getParentCategory(), subCategory.getSubCategories(), subCategory.getCategoryProducts());
//				}
//			}
//		}
//		if(category.getParentCategory() != null)
//		{
//			Categories parentCategory = catRepo.getCategoryRepo(category.getParentCategory());
//			parentCategory.getSubCategories().remove(categoryId);
//			catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
//		}
//		
//		catRepo.deleteCategoryRepo(categoryId);
//	}
	
//	public void addCategoryService(Map<String, Object> body)
//	{
//		UUID categoryId = Uuids.timeBased();
//		String categoryName = "";
//		String categoryImage = "";
//		UUID parentCategoryId = null;
//		List<UUID> categorySubCategories = new ArrayList<>();
//		List<Pobject> categoryProducts = new ArrayList<>();
//		
//		if(body.containsKey("name"))
//			categoryName = (String)body.get("name");
//		
//		if(body.containsKey("image"))
//			categoryImage = (String)body.get("image");
//		
//		catRepo.addCategoryRepo(categoryId, categoryName, categoryImage, parentCategoryId, categorySubCategories, categoryProducts);
//	}
	
//	public void addCategoryService(Map<String, Object> body)
//	{
//		UUID categoryId = Uuids.timeBased();
//		String categoryName = "";
//		String categoryImage = "";
//		UUID parentCategoryId = null;
//		List<UUID> categorySubCategories = new ArrayList<>();
//		List<Pobject> categoryProducts = new ArrayList<>();
//		
//		if(body.containsKey("parentcategory"))
//			parentCategoryId = UUID.fromString((String)body.get("parentcategory"));
//		
//		if(body.containsKey("name"))
//			categoryName = (String)body.get("name");
//		
//		if(body.containsKey("image"))
//			categoryImage = (String)body.get("image");
//		
//		
//		if(parentCategoryId != null)
//		{	
//			Categories parentCategory = catRepo.getCategoryRepo(parentCategoryId);
//			if(parentCategory.getSubCategories() == null)
//			{
//				List<UUID> parentSubCategories = new ArrayList<>();
//				parentSubCategories.add(categoryId);
//				parentCategory.setSubCategories(parentSubCategories);
//				
//				if(parentCategory.getCategoryProducts() != null)
//				{	
//					for(Pobject prod: parentCategory.getCategoryProducts())
//					{
//						Products product = prodRepo.getProductRepo(prod.getId());
//						product.setParentCategory(categoryId);
//						prodRepo.updateProductRepo(product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getDiscount(), product.getDescription(), product.getBrand(), product.getParentCategory());
//						
//						categoryProducts.add(prod);
//					}
//					
//					parentCategory.getCategoryProducts().clear();
//				}
//			}
//			else
//				parentCategory.getSubCategories().add(categoryId);
//			
//			catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
//		}
//		
//		catRepo.addCategoryRepo(categoryId, categoryName, categoryImage, parentCategoryId, categorySubCategories, categoryProducts);
//	}
	
//	public void updateCategoryService(Map<String, Object> body)
//	{
//		UUID categoryId = UUID.fromString((String)body.get("id"));
//		String newCategoryName;
//		String newCategoryImage;
//		UUID newParentCategoryId;
//		List<UUID> newCategorySubCategories;
//		List<Pobject> newCategoryProducts;
//		
//		Categories category = catRepo.getCategoryRepo(categoryId);
//		
//		if(body.containsKey("name"))
//			newCategoryName = (String)body.get("name");
//		else
//			newCategoryName = category.getName();
//		
//		if(body.containsKey("image"))
//			newCategoryImage = (String)body.get("image");
//		else
//			newCategoryImage = category.getImage();
//		
//		newParentCategoryId = category.getParentCategory();
//		newCategorySubCategories = category.getSubCategories();
//		newCategoryProducts = category.getCategoryProducts();
//		
//		catRepo.updateCategoryRepo(categoryId, newCategoryName, newCategoryImage, newParentCategoryId, newCategorySubCategories, newCategoryProducts);
//	}
	
	// --------------------------------------------- END CATEGORIES ---------------------------------------------
	
	
	// --------------------------------------------- PRODUCTS ------------------------------------------------
	
	
	public Slice<Products> listProductsService(int page, int size) {
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
	
	public Products getProductService(Map<String, Object> body)
	{
		UUID productId = UUID.fromString((String)body.get("id"));
		Products returnedProduct = prodRepo.getProductRepo(productId);
		return returnedProduct;
	}
	
	public List<Pobject> listCategoryProductsService(Map<String, Object> body)
	{
		UUID categoryId = UUID.fromString((String)body.get("id"));
		Categories category = catRepo.getCategoryRepo(categoryId);
		List<Pobject> categoryProducts = new ArrayList<>();
		
		if(category.getCategoryProducts() != null)
		{
			for(Pobject product: category.getCategoryProducts())
			{
				categoryProducts.add(product);
			}
		}
		else
		{
			Stack<UUID> stack = new Stack<>();
			stack.push(categoryId);
			
			while(!stack.isEmpty())
			{
				UUID poppedCategoryId = stack.pop();
				Categories poppedCategory = catRepo.getCategoryRepo(poppedCategoryId);
				
				if(poppedCategory.getSubCategories() != null)
				{
					for(UUID subCategoryId: poppedCategory.getSubCategories())
					{
						stack.push(subCategoryId);
					}
				}
				else 
				{
					if(poppedCategory.getCategoryProducts() != null)
					{
						for(Pobject product: poppedCategory.getCategoryProducts())
						{
							categoryProducts.add(product);
						}
					}
				}
			}
		}
		return categoryProducts;
	}
	
	 public void deleteProductService(Map<String, Object> body) {
		
		UUID productId = UUID.fromString((String)body.get("id"));
		Products product = prodRepo.getProductRepo(productId);
		if(product.getParentCategory() != null)
		{
			Categories parentCategory = catRepo.getCategoryRepo(product.getParentCategory());
			for(Pobject prod: parentCategory.getCategoryProducts())
			{
				if(prod.getId().equals(productId))
				{
					parentCategory.getCategoryProducts().remove(prod);
					break;
				}
			}
			catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
		}
		if(product.getBrand() != null)
		{
			Brands brand = brandRepo.getBrandRepo(product.getBrand());
			for(Pobject prod: brand.getBrandProducts())
			{
				if(prod.getId().equals(productId))
				{
					brand.getBrandProducts().remove(prod);
					break;
				}
			}
			brandRepo.updateBrandRepo(brand.getId(), brand.getName(), brand.getBrandProducts());
		}
		
	    prodRepo.deleteProductRepo(productId);
	}
	
	public void addProductService(Map<String, Object> body)
	{
		UUID categoryId = null;
		UUID productId = Uuids.timeBased();
	    String productName = "";
	    String productImage = "";
	    double productPrice = 0;
	    String productDiscount = "";
	    String productDescription = "";
	    UUID brandId = null;
	    String brandName = "";
	    
	    
	    if(body.containsKey("parentcategory"))
	    {
	    	categoryId = UUID.fromString((String)body.get("parentcategory"));
	    }
	    if(body.containsKey("name"))
	    {
	    	productName = (String)body.get("name");
	    }
	    if(body.containsKey("image"))
	    {
	    	productImage = (String)body.get("image");
	    }
	    if(body.containsKey("price"))
	    {
	    	productPrice = (double)body.get("price");
	    }
	    if(body.containsKey("discount"))
	    {
	    	productDiscount = (String)body.get("discount");
	    }
	    if(body.containsKey("description"))
	    {
	    	productDescription = (String)body.get("description");
	    }
	    if(body.containsKey("brand"))
	    {
	    	brandId = UUID.fromString((String)body.get("brand"));
	    	Brands brand = brandRepo.getBrandRepo(brandId);
	    	brandName = brand.getName();
	    	
	    	Pobject productObject = new Pobject(productId, productName, productImage, brandName, productPrice, productDiscount);
	    	if(brand.getBrandProducts() == null)
	    	{
	    		List<Pobject> brandProducts = new ArrayList<>();
	    		brandProducts.add(productObject);
	    		brand.setBrandProducts(brandProducts);
	    	}
	    	else
	    		brand.getBrandProducts().add(productObject);
	    	
	    	brandRepo.updateBrandRepo(brand.getId(), brand.getName(), brand.getBrandProducts());
	    }
	    
	    if(categoryId != null)
	    {
	    	Categories parentCategory = catRepo.getCategoryRepo(categoryId);
		    Pobject productObject = new Pobject(productId, productName, productImage, brandName, productPrice, productDiscount);
	    	
	    	if(parentCategory.getCategoryProducts() == null)
	    	{
	    		List<Pobject> parentProducts = new ArrayList<>();
	    		parentProducts.add(productObject);
				parentCategory.setCategoryProducts(parentProducts);
	    	}
	    	else
	    		parentCategory.getCategoryProducts().add(productObject);
	    	
	    	catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
	    }
	    
	    prodRepo.addProductRepo(productId, productName, productImage, productPrice, productDiscount, productDescription, brandId, categoryId);
    }
	
	public void updateProductService(Map<String, Object> body)
	{
		String newProductName;
		String newProductImage;
		double newProductPrice;
		String newProductDiscount;
		String newProductDescription;
		UUID newBrandId;
		UUID newParentCategoryId;
		
		UUID productId = UUID.fromString((String)body.get("id"));
		Products product = prodRepo.getProductRepo(productId);
		
		if(body.containsKey("name"))
		{
			newProductName = (String)body.get("name");
			
			if(product.getParentCategory() != null)
			{
				UUID currentCategoryId = product.getParentCategory();
				Categories cat = catRepo.getCategoryRepo(currentCategoryId);
				
				for(Pobject productObject: cat.getCategoryProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setName(newProductName);
						break;
					}
				}
				catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
			}
			if(product.getBrand() != null)
			{
				UUID currentBrandId = product.getBrand();
				Brands br = brandRepo.getBrandRepo(currentBrandId);
				
				for(Pobject productObject: br.getBrandProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setName(newProductName);
						break;
					}
				}
				brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
			}
		}
		else
			newProductName = product.getName();
		
		if(body.containsKey("image"))
		{
			newProductImage = (String)body.get("image");
			
			if(product.getParentCategory() != null)
			{
				UUID currentCategoryId = product.getParentCategory();
				Categories cat = catRepo.getCategoryRepo(currentCategoryId);
				
				for(Pobject productObject: cat.getCategoryProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setImage(newProductImage);
						break;
					}
				}
				catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
			}
			if(product.getBrand() != null)
			{
				UUID currentBrandId = product.getBrand();
				Brands br = brandRepo.getBrandRepo(currentBrandId);
				
				for(Pobject productObject: br.getBrandProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setImage(newProductImage);
						break;
					}
				}
				brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
			}
		}
		else
			newProductImage = product.getImage();
		
		if(body.containsKey("price"))
		{
			newProductPrice = (double)body.get("price");
			
			if(product.getParentCategory() != null)
			{
				UUID currentCategoryId = product.getParentCategory();
				Categories cat = catRepo.getCategoryRepo(currentCategoryId);
				
				for(Pobject productObject: cat.getCategoryProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setPrice(newProductPrice);
						break;
					}
				}
				catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
			}
			if(product.getBrand() != null)
			{
				UUID currentBrandId = product.getBrand();
				Brands br = brandRepo.getBrandRepo(currentBrandId);
				
				for(Pobject productObject: br.getBrandProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setPrice(newProductPrice);
						break;
					}
				}
				brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
			}
		}
		else
			newProductPrice = product.getPrice();
		
		if(body.containsKey("discount"))
		{
			newProductDiscount = (String)body.get("discount");
			
			if(product.getParentCategory() != null)
			{
				UUID currentCategoryId = product.getParentCategory();
				Categories cat = catRepo.getCategoryRepo(currentCategoryId);
				
				for(Pobject productObject: cat.getCategoryProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setDiscount(newProductDiscount);
						break;
					}
				}
				catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
			}
			if(product.getBrand() != null)
			{
				UUID currentBrandId = product.getBrand();
				Brands br = brandRepo.getBrandRepo(currentBrandId);
				
				for(Pobject productObject: br.getBrandProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setDiscount(newProductDiscount);
						break;
					}
				}
				brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
			}
		}
		else
			newProductDiscount = product.getDiscount();
		
		if(body.containsKey("description"))
			newProductDescription = (String)body.get("description");
		else
			newProductDescription = product.getDescription();
		
		if(body.containsKey("brand"))
		{
			newBrandId = UUID.fromString((String)body.get("brand"));
			Brands newBrand = brandRepo.getBrandRepo(newBrandId);
			
			if(product.getBrand() != null)
			{
				Brands oldBrand = brandRepo.getBrandRepo(product.getBrand());
				
				for(Pobject productObject: oldBrand.getBrandProducts())
				{
					if(productObject.getId().equals(productId))
					{
						productObject.setBrandName(newBrand.getName());
						
						if(newBrand.getBrandProducts() == null)
				    	{
				    		List<Pobject> brandProducts = new ArrayList<>();
				    		brandProducts.add(productObject);
				    		newBrand.setBrandProducts(brandProducts);
				    	}
				    	else
				    		newBrand.getBrandProducts().add(productObject);
						
						oldBrand.getBrandProducts().remove(productObject);
						
						break;
					}
				}
				brandRepo.updateBrandRepo(oldBrand.getId(), oldBrand.getName(), oldBrand.getBrandProducts());
				brandRepo.updateBrandRepo(newBrand.getId(), newBrand.getName(), newBrand.getBrandProducts());
			}
			else
			{
			    Pobject productObject = new Pobject(productId, newProductName, newProductImage, newBrand.getName(), newProductPrice, newProductDiscount);
			    
			    if(newBrand.getBrandProducts() == null)
		    	{
		    		List<Pobject> brandProducts = new ArrayList<>();
		    		brandProducts.add(productObject);
		    		newBrand.setBrandProducts(brandProducts);
		    	}
		    	else
		    		newBrand.getBrandProducts().add(productObject);
			    
			    brandRepo.updateBrandRepo(newBrand.getId(), newBrand.getName(), newBrand.getBrandProducts());
			}
			
			if(product.getParentCategory() != null)
			{
				Categories parentCategory = catRepo.getCategoryRepo(product.getParentCategory());
				
				for(Pobject productObject : parentCategory.getCategoryProducts())
				{
					if(productObject.getId().equals(product.getId()))
					{
						productObject.setBrandName(newBrand.getName());
						catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
						break;
					}
				}
			}
		}
		else
			newBrandId = product.getBrand();
		
		if(body.containsKey("parentcategory"))
		{
			newParentCategoryId = UUID.fromString((String)body.get("parentcategory"));
			Categories newParentCategory = catRepo.getCategoryRepo(newParentCategoryId);
			
			if(product.getParentCategory() != null)
			{
				Categories oldParentCategory = catRepo.getCategoryRepo(product.getParentCategory());
				
				for(Pobject prod: oldParentCategory.getCategoryProducts())
				{
					if(prod.getId().equals(productId))
					{
						if(newParentCategory.getCategoryProducts() == null)
				    	{
				    		List<Pobject> parentProducts = new ArrayList<>();
				    		parentProducts.add(prod);
				    		newParentCategory.setCategoryProducts(parentProducts);
				    	}
				    	else
				    		newParentCategory.getCategoryProducts().add(prod);
						
						oldParentCategory.getCategoryProducts().remove(prod);
						
						break;
					}
				}
				catRepo.updateCategoryRepo(oldParentCategory.getId(), oldParentCategory.getName(), oldParentCategory.getImage(), oldParentCategory.getParentCategory(), oldParentCategory.getSubCategories(), oldParentCategory.getCategoryProducts());
				catRepo.updateCategoryRepo(newParentCategory.getId(), newParentCategory.getName(), newParentCategory.getImage(), newParentCategory.getParentCategory(), newParentCategory.getSubCategories(), newParentCategory.getCategoryProducts());
			}
			else
			{
				if(newBrandId != null)
				{
					Brands brand = brandRepo.getBrandRepo(newBrandId);
				    Pobject productObject = new Pobject(productId, newProductName, newProductImage, brand.getName(), newProductPrice, newProductDiscount);
				    
				    if(newParentCategory.getCategoryProducts() == null)
			    	{
			    		List<Pobject> parentProducts = new ArrayList<>();
			    		parentProducts.add(productObject);
			    		newParentCategory.setCategoryProducts(parentProducts);
			    	}
			    	else
			    		newParentCategory.getCategoryProducts().add(productObject);
				    
				    catRepo.updateCategoryRepo(newParentCategory.getId(), newParentCategory.getName(), newParentCategory.getImage(), newParentCategory.getParentCategory(), newParentCategory.getSubCategories(), newParentCategory.getCategoryProducts());
				}
				else
				{
					Pobject productObject = new Pobject(productId, newProductName, newProductImage, "", newProductPrice, newProductDiscount);
				    
				    if(newParentCategory.getCategoryProducts() == null)
			    	{
			    		List<Pobject> parentProducts = new ArrayList<>();
			    		parentProducts.add(productObject);
			    		newParentCategory.setCategoryProducts(parentProducts);
			    	}
			    	else
			    		newParentCategory.getCategoryProducts().add(productObject);
				    
				    catRepo.updateCategoryRepo(newParentCategory.getId(), newParentCategory.getName(), newParentCategory.getImage(), newParentCategory.getParentCategory(), newParentCategory.getSubCategories(), newParentCategory.getCategoryProducts());
				}
			}
		}
		else
			newParentCategoryId = product.getParentCategory();
		
		prodRepo.updateProductRepo(productId, newProductName, newProductImage, newProductPrice, newProductDiscount, newProductDescription, newBrandId, newParentCategoryId);
	}
	
	
	// --------------------------------------------- END PRODUCTS ---------------------------------------------
	 
	 
	 
	// --------------------------------------------- BRANDS ----------------------------------------------------
	 
	public List<Brands> listBrandService()
	{
		List<Brands> returnedBrands = brandRepo.listBrandRepo();
		return returnedBrands;
	}
		
	public Brands getBrandService(Map<String, Object> body)
	{
		UUID brandId = UUID.fromString((String)body.get("id"));
		Brands returnedBrand = brandRepo.getBrandRepo(brandId);
		return returnedBrand;
	}
	
	public List<Pobject> listBrandProductsService(Map<String, Object> body)
	{
		UUID brandId = UUID.fromString((String)body.get("id"));
		Brands brand = brandRepo.getBrandRepo(brandId);
		List<Pobject> brandProducts = new ArrayList<>();
		
		if(brand.getBrandProducts() != null)
		{
			for(Pobject product: brand.getBrandProducts())
			{
				brandProducts.add(product);
			}
		}
		return brandProducts;
	}
	
	public void deleteBrandService(Map<String, Object> body)
	{
		UUID brandId = UUID.fromString((String)body.get("id"));
		Brands brand = brandRepo.getBrandRepo(brandId);
		
		//Set all products to be null parent category if had products
		if(brand.getBrandProducts() != null)
		{
			for(Pobject prod : brand.getBrandProducts())
			{
				Products product = prodRepo.getProductRepo(prod.getId());
				UUID parentCategoryId = product.getParentCategory();
				Categories parentCategory = catRepo.getCategoryRepo(parentCategoryId);
				for(Pobject categoryProd : parentCategory.getCategoryProducts())
				{
					if(categoryProd.getId().equals(prod.getId()))
					{
						categoryProd.setBrandName("");
						catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
						break;
					}
				}
				product.setBrand(null);
				prodRepo.updateProductRepo(product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getDiscount(), product.getDescription(), product.getBrand(), product.getParentCategory());
			}
		}
		brandRepo.deleteBrandRepo(brandId);
	}
	
	public void addBrandService(Map<String, Object> body)
	{
		UUID brandId = Uuids.timeBased();
		String brandName = "";
		ArrayList<Pobject> brandProducts = new ArrayList<>();
		
		if(body.containsKey("name"))
			brandName = (String)body.get("name");
		
		brandRepo.addBrandRepo(brandId,brandName,brandProducts);
	}
	
	public void updateBrandService(Map<String, Object> body)
	{
		String newBrandName;
		
		UUID brandId = UUID.fromString((String)body.get("id"));
		Brands brand = brandRepo.getBrandRepo(brandId);
		
		if(body.containsKey("name"))
		{
			newBrandName = (String)body.get("name");
			
			for(Pobject prod : brand.getBrandProducts())
			{
				Products product = prodRepo.getProductRepo(prod.getId());
				UUID parentCategoryId = product.getParentCategory();
				Categories parentCategory = catRepo.getCategoryRepo(parentCategoryId);
				
				for(Pobject categoryProd : parentCategory.getCategoryProducts())
				{
					if(categoryProd.getId().equals(prod.getId()))
					{
						categoryProd.setBrandName(newBrandName);
						catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
						break;
					}
				}
				
				prod.setBrandName(newBrandName);
			}
		}
		else
			newBrandName = brand.getName();
		
		brandRepo.updateBrandRepo(brandId, newBrandName, brand.getBrandProducts());
	}
		// --------------------------------------------- END BRANDS -------------------------------------------------
}
