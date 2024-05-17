package com.example.demo.cassandraCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

public class listBrandProductsCommand implements Command{
	
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	@Autowired
	public listBrandProductsCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
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
			UUID brandId = UUID.fromString((String)body.get("parameter"));
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
		
		return null;
	}

}
