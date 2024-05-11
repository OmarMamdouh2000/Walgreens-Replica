package com.example.demo.cassandraCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class addProductCommand implements Command{
	
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public addProductCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
	{
		this.catRepo=catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		if(body.containsKey("name"))
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
		    	if(brand != null) {
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
		    }
		    
		    if(categoryId != null)
		    {
		    	Categories parentCategory = catRepo.getCategoryRepo(categoryId);
			    Pobject productObject = new Pobject(productId, productName, productImage, brandName, productPrice, productDiscount);
		    	
			    if(parentCategory != null) {
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
		    }
		    
		    prodRepo.addProductRepo(productId, productName, productImage, productPrice, productDiscount, productDescription, brandId, categoryId);
			return("Success");
		}
		return("Failed");
	}

}
