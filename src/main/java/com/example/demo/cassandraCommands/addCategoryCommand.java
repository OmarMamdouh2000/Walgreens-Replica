package com.example.demo.cassandraCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class addCategoryCommand implements Command{
	
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public addCategoryCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
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
			UUID categoryId = Uuids.timeBased();
			String categoryName = "";
			String categoryImage = "";
			UUID parentCategoryId = null;
			List<UUID> categorySubCategories = new ArrayList<>();
			List<Pobject> categoryProducts = new ArrayList<>();
			
			if(body.containsKey("parentcategory"))
				parentCategoryId = UUID.fromString((String)body.get("parentcategory"));
			
			if(body.containsKey("name"))
				categoryName = (String)body.get("name");
			
			if(body.containsKey("image"))
				categoryImage = (String)body.get("image");
			
			
			if(parentCategoryId != null)
			{	
				Categories parentCategory = catRepo.getCategoryRepo(parentCategoryId);
				if(parentCategory.getSubCategories() == null)
				{
					List<UUID> parentSubCategories = new ArrayList<>();
					parentSubCategories.add(categoryId);
					parentCategory.setSubCategories(parentSubCategories);
					
					if(parentCategory.getCategoryProducts() != null)
					{	
						for(Pobject prod: parentCategory.getCategoryProducts())
						{
							Products product = prodRepo.getProductRepo(prod.getId());
							product.setParentCategory(categoryId);
							prodRepo.updateProductRepo(product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getDiscount(), product.getDescription(), product.getBrand(), product.getParentCategory());
							
							categoryProducts.add(prod);
						}
						
						parentCategory.getCategoryProducts().clear();
					}
				}
				else
					parentCategory.getSubCategories().add(categoryId);
				
				catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
			}
			
			catRepo.addCategoryRepo(categoryId, categoryName, categoryImage, parentCategoryId, categorySubCategories, categoryProducts);
			return("Success");
		}
		return("Failed");
	}

}
