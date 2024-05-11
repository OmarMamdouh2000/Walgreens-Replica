package com.example.demo.cassandraCommands;

import java.util.Map;
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
public class deleteBrandCommand implements Command{
	
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public deleteBrandCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo) 
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
			Categories category = catRepo.getCategoryRepo(categoryId);
			
			//Set all products to be null parent category if had products
			if(category.getCategoryProducts() != null)
			{
				for(Pobject prod: category.getCategoryProducts())
				{
					Products product = prodRepo.getProductRepo(prod.getId());
					product.setParentCategory(null);
					prodRepo.updateProductRepo(product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getDiscount(), product.getDescription(), product.getBrand(), product.getParentCategory());
				}
			}
			
			if(category.getSubCategories() != null)
			{
				if(category.getParentCategory() != null)
				{
					Categories parentCategory = catRepo.getCategoryRepo(category.getParentCategory());
					for(UUID subCategoryId: category.getSubCategories())
					{
						Categories subCategory = catRepo.getCategoryRepo(subCategoryId);
						subCategory.setParentCategory(parentCategory.getId());
						catRepo.updateCategoryRepo(subCategory.getId(), subCategory.getName(), subCategory.getImage(), subCategory.getParentCategory(), subCategory.getSubCategories(), subCategory.getCategoryProducts());
						
						parentCategory.getSubCategories().add(subCategoryId);
					}
					
					parentCategory.getSubCategories().remove(categoryId);
					catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
				}
				else
				{
					for(UUID subCategoryId: category.getSubCategories())
					{
						Categories subCategory = catRepo.getCategoryRepo(subCategoryId);
						subCategory.setParentCategory(null);
						catRepo.updateCategoryRepo(subCategory.getId(), subCategory.getName(), subCategory.getImage(), subCategory.getParentCategory(), subCategory.getSubCategories(), subCategory.getCategoryProducts());
					}
				}
			}
			if(category.getParentCategory() != null)
			{
				Categories parentCategory = catRepo.getCategoryRepo(category.getParentCategory());
				parentCategory.getSubCategories().remove(categoryId);
				catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
			}
			
			catRepo.deleteCategoryRepo(categoryId);
			return "Success";
		}
		return "Failed";
	}

}
