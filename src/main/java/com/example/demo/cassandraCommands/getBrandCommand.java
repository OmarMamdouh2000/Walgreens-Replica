package com.example.demo.cassandraCommands;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

public class getBrandCommand implements Command {
	
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	@Autowired
	public getBrandCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
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
			UUID brandID = UUID.fromString((String)body.get("parameter"));
			Brands thisBrand = brandRepo.getBrandRepo(brandID);
			return thisBrand;
		}
		
		return null;
	}

}
