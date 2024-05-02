package com.example.demo.cassandraCommands;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class updateCategoryCommand implements Command{

	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public updateCategoryCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
	{
		this.catRepo=catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		if(body.containsKey("parameter"))
		{
			UUID categoryId = UUID.fromString((String)body.get("parameter"));
			String newCategoryName;
			String newCategoryImage;
			UUID newParentCategoryId;
			List<UUID> newCategorySubCategories;
			List<Pobject> newCategoryProducts;
			
			Categories category = catRepo.getCategoryRepo(categoryId);
			
			if(body.containsKey("name"))
				newCategoryName = (String)body.get("name");
			else
				newCategoryName = category.getName();
			
			if(body.containsKey("image"))
				newCategoryImage = (String)body.get("image");
			else
				newCategoryImage = category.getImage();
			
			newParentCategoryId = category.getParentCategory();
			newCategorySubCategories = category.getSubCategories();
			newCategoryProducts = category.getCategoryProducts();
			
			catRepo.updateCategoryRepo(categoryId, newCategoryName, newCategoryImage, newParentCategoryId, newCategorySubCategories, newCategoryProducts);
			return("Success");
		}
		return("Failed");
	}
}
