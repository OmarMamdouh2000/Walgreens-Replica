package com.example.demo.cassandraCommands;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.cassandraFirebase.FirebaseService;
import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;
import com.example.demo.cassandraModels.Products;
import com.example.demo.cassandraRepositories.BrandsRepo;
import com.example.demo.cassandraRepositories.CategoriesRepo;
import com.example.demo.cassandraRepositories.ProductsRepo;

@Service
public class deleteBrandCommand implements Command{
	
	private FirebaseService firebaseService;
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public deleteBrandCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo, FirebaseService firebaseService) 
	{
		this.catRepo=catRepo;
		this.prodRepo = prodRepo;
		this.brandRepo = brandRepo;
		this.firebaseService = firebaseService;
	}
	
	@Override
	public Object execute(Map<String,Object> body) 
	{
		if(body.containsKey("parameter"))
		{
			UUID brandId = UUID.fromString((String)body.get("parameter"));
			Brands brand = brandRepo.getBrandRepo(brandId);
			
			//Set all products to be null parent category if had products
			if(brand.getBrandProducts() != null)
			{
				for(Pobject prod : brand.getBrandProducts())
				{
					Products product = prodRepo.getProductRepo(prod.getId());
					UUID parentCategoryId = product.getParentCategory();
					Categories parentCategory = catRepo.getCategoryRepo(parentCategoryId);
					for(Pobject categoryProd : parentCategory.getCategoryProducts())
					{
						if(categoryProd.getId().equals(prod.getId()))
						{
							categoryProd.setBrandName("");
							catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
							break;
						}
					}
					product.setBrand(null);
					prodRepo.updateProductRepo(product.getId(), product.getName(), product.getImage(), product.getPrice(), product.getDiscount(), product.getDescription(), product.getBrand(), product.getParentCategory());
				}
			}
			brandRepo.deleteBrandRepo(brandId);
			return "Success";
		}
		return "Failed";
	}

}
