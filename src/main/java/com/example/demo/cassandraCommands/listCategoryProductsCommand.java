package com.example.demo.cassandraCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class listCategoryProductsCommand implements Command{

	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public listCategoryProductsCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
	{
		this.catRepo= catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		List<Pobject> categoryProducts = new ArrayList<>();

		if(body.containsKey("parameter"))
		{
			UUID categoryId = UUID.fromString((String)body.get("parameter"));
			Categories category = catRepo.getCategoryRepo(categoryId);
			
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
		}
		return categoryProducts;
	}
}
