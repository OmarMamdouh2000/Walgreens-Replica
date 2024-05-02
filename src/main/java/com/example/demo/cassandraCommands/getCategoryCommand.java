package com.example.demo.cassandraCommands;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class getCategoryCommand implements Command{
	
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public getCategoryCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
	{
		this.catRepo=catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		if(body.containsKey("id"))
		{
			UUID categoryId = UUID.fromString((String)body.get("id"));
			Categories returnedCategory = catRepo.getCategoryRepo(categoryId);
			return returnedCategory;
		}
		
		return null;
	}

}
