package com.example.demo.cassandraCommands;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraFirebase.FirebaseService;
import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class listBrandCommand implements Command {
	
	private FirebaseService firebaseService;
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	@Autowired
	public listBrandCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo, FirebaseService firebaseService) 
	{
		this.catRepo=catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
		this.firebaseService = firebaseService;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		List<Brands> thisBrand = brandRepo.listBrandRepo();
		return thisBrand;
	}

}
