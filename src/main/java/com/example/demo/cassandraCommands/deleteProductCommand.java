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
public class deleteProductCommand implements Command{
	
	private FirebaseService firebaseService;
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public deleteProductCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo, FirebaseService firebaseService) 
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
			UUID productId = UUID.fromString((String)body.get("parameter"));
			Products product = prodRepo.getProductRepo(productId);
			if(product != null) {
				if(product.getParentCategory() != null)
				{
					Categories parentCategory = catRepo.getCategoryRepo(product.getParentCategory());
					for(Pobject prod: parentCategory.getCategoryProducts())
					{
						if(prod.getId().equals(productId))
						{
							parentCategory.getCategoryProducts().remove(prod);
							break;
						}
					}
					catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
				}
				if(product.getBrand() != null)
				{
					Brands brand = brandRepo.getBrandRepo(product.getBrand());
					for(Pobject prod: brand.getBrandProducts())
					{
						if(prod.getId().equals(productId))
						{
							brand.getBrandProducts().remove(prod);
							break;
						}
					}
					brandRepo.updateBrandRepo(brand.getId(), brand.getName(), brand.getBrandProducts());
				}
			    prodRepo.deleteProductRepo(productId);
				return "Success";
			}
			else {
				return "Product Not Found";
			}
		}
		return "Failed";
	}

}
