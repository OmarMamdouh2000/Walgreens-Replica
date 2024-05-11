package com.example.demo.cassandraCommands;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

public class addBrandCommand implements Command{
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	@Autowired
	public addBrandCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
	{
		this.catRepo=catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		if(body.containsKey("name")) {
			UUID brandId = Uuids.timeBased();
			String brandName = "";
			ArrayList<Pobject> products = new ArrayList<>();
			
			if(body.containsKey("name"))
				brandName = (String)body.get("name");
			
			brandRepo.addBrandRepo(brandId,brandName,products);
			return("Success");
		
		}
		return("Failed");

	}
}
