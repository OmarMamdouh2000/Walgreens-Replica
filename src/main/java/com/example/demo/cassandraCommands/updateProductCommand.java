package com.example.demo.cassandraCommands;

import java.util.ArrayList;
import java.util.List;
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
public class updateProductCommand implements Command{

	private FirebaseService firebaseService;
	private CategoriesRepo catRepo;
	private ProductsRepo prodRepo;
	private BrandsRepo brandRepo;
	
	
	@Autowired
	public updateProductCommand(CategoriesRepo catRepo, ProductsRepo prodRepo, BrandsRepo brandRepo, FirebaseService firebaseService) 
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
			String newProductName;
			String newProductImage;
			double newProductPrice;
			String newProductDiscount;
			String newProductDescription;
			UUID newBrandId;
			UUID newParentCategoryId;
			
			UUID productId = UUID.fromString((String)body.get("parameter"));
			Products product = prodRepo.getProductRepo(productId);
			
			if(body.containsKey("name"))
			{
				newProductName = (String)body.get("name");
				
				if(product.getParentCategory() != null)
				{
					UUID currentCategoryId = product.getParentCategory();
					Categories cat = catRepo.getCategoryRepo(currentCategoryId);
					
					for(Pobject productObject: cat.getCategoryProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setName(newProductName);
							break;
						}
					}
					catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
				}
				if(product.getBrand() != null)
				{
					UUID currentBrandId = product.getBrand();
					Brands br = brandRepo.getBrandRepo(currentBrandId);
					
					for(Pobject productObject: br.getBrandProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setName(newProductName);
							break;
						}
					}
					brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
				}
			}
			else
				newProductName = product.getName();
			
			if(body.containsKey("image"))
			{
				newProductImage = (String)body.get("image");
				
				if(product.getParentCategory() != null)
				{
					UUID currentCategoryId = product.getParentCategory();
					Categories cat = catRepo.getCategoryRepo(currentCategoryId);
					
					for(Pobject productObject: cat.getCategoryProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setImage(newProductImage);
							break;
						}
					}
					catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
				}
				if(product.getBrand() != null)
				{
					UUID currentBrandId = product.getBrand();
					Brands br = brandRepo.getBrandRepo(currentBrandId);
					
					for(Pobject productObject: br.getBrandProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setImage(newProductImage);
							break;
						}
					}
					brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
				}
			}
			else
				newProductImage = product.getImage();
			
			if(body.containsKey("price"))
			{
				newProductPrice = (double)body.get("price");
				
				if(product.getParentCategory() != null)
				{
					UUID currentCategoryId = product.getParentCategory();
					Categories cat = catRepo.getCategoryRepo(currentCategoryId);
					
					for(Pobject productObject: cat.getCategoryProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setPrice(newProductPrice);
							break;
						}
					}
					catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
				}
				if(product.getBrand() != null)
				{
					UUID currentBrandId = product.getBrand();
					Brands br = brandRepo.getBrandRepo(currentBrandId);
					
					for(Pobject productObject: br.getBrandProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setPrice(newProductPrice);
							break;
						}
					}
					brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
				}
			}
			else
				newProductPrice = product.getPrice();
			
			if(body.containsKey("discount"))
			{
				newProductDiscount = (String)body.get("discount");
				
				if(product.getParentCategory() != null)
				{
					UUID currentCategoryId = product.getParentCategory();
					Categories cat = catRepo.getCategoryRepo(currentCategoryId);
					
					for(Pobject productObject: cat.getCategoryProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setDiscount(newProductDiscount);
							break;
						}
					}
					catRepo.updateCategoryRepo(cat.getId(), cat.getName(), cat.getImage(), cat.getParentCategory(), cat.getSubCategories(), cat.getCategoryProducts());
				}
				if(product.getBrand() != null)
				{
					UUID currentBrandId = product.getBrand();
					Brands br = brandRepo.getBrandRepo(currentBrandId);
					
					for(Pobject productObject: br.getBrandProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setDiscount(newProductDiscount);
							break;
						}
					}
					brandRepo.updateBrandRepo(br.getId(), br.getName(), br.getBrandProducts());
				}
			}
			else
				newProductDiscount = product.getDiscount();
			
			if(body.containsKey("description"))
				newProductDescription = (String)body.get("description");
			else
				newProductDescription = product.getDescription();
			
			if(body.containsKey("brand"))
			{
				newBrandId = UUID.fromString((String)body.get("brand"));
				Brands newBrand = brandRepo.getBrandRepo(newBrandId);
				
				if(product.getBrand() != null)
				{
					Brands oldBrand = brandRepo.getBrandRepo(product.getBrand());
					
					for(Pobject productObject: oldBrand.getBrandProducts())
					{
						if(productObject.getId().equals(productId))
						{
							productObject.setBrandName(newBrand.getName());
							
							if(newBrand.getBrandProducts() == null)
					    	{
					    		List<Pobject> brandProducts = new ArrayList<>();
					    		brandProducts.add(productObject);
					    		newBrand.setBrandProducts(brandProducts);
					    	}
					    	else
					    		newBrand.getBrandProducts().add(productObject);
							
							if(!oldBrand.getId().equals(newBrand.getId()))
							{
								oldBrand.getBrandProducts().remove(productObject);
							}
							
							break;
						}
					}
					if(oldBrand.getId().equals(newBrand.getId()))
					{
						brandRepo.updateBrandRepo(oldBrand.getId(), oldBrand.getName(), oldBrand.getBrandProducts());
					}
					else
					{
						brandRepo.updateBrandRepo(oldBrand.getId(), oldBrand.getName(), oldBrand.getBrandProducts());
						brandRepo.updateBrandRepo(newBrand.getId(), newBrand.getName(), newBrand.getBrandProducts());
					}
				}
				else
				{
				    Pobject productObject = new Pobject(productId, newProductName, newProductImage, newBrand.getName(), newProductPrice, newProductDiscount);
				    
				    if(newBrand.getBrandProducts() == null)
			    	{
			    		List<Pobject> brandProducts = new ArrayList<>();
			    		brandProducts.add(productObject);
			    		newBrand.setBrandProducts(brandProducts);
			    	}
			    	else
			    		newBrand.getBrandProducts().add(productObject);
				    
				    brandRepo.updateBrandRepo(newBrand.getId(), newBrand.getName(), newBrand.getBrandProducts());
				}
				
				if(product.getParentCategory() != null)
				{
					Categories parentCategory = catRepo.getCategoryRepo(product.getParentCategory());
					
					for(Pobject productObject : parentCategory.getCategoryProducts())
					{
						if(productObject.getId().equals(product.getId()))
						{
							productObject.setBrandName(newBrand.getName());
							catRepo.updateCategoryRepo(parentCategory.getId(), parentCategory.getName(), parentCategory.getImage(), parentCategory.getParentCategory(), parentCategory.getSubCategories(), parentCategory.getCategoryProducts());
							break;
						}
					}
				}
			}
			else
				newBrandId = product.getBrand();
			
			if(body.containsKey("parentcategory"))
			{
				newParentCategoryId = UUID.fromString((String)body.get("parentcategory"));
				Categories newParentCategory = catRepo.getCategoryRepo(newParentCategoryId);
				
				if(product.getParentCategory() != null)
				{
					Categories oldParentCategory = catRepo.getCategoryRepo(product.getParentCategory());
					
					for(Pobject prod: oldParentCategory.getCategoryProducts())
					{
						if(prod.getId().equals(productId))
						{
							if(newParentCategory.getCategoryProducts() == null)
					    	{
					    		List<Pobject> parentProducts = new ArrayList<>();
					    		parentProducts.add(prod);
					    		newParentCategory.setCategoryProducts(parentProducts);
					    	}
					    	else
					    		newParentCategory.getCategoryProducts().add(prod);
							
							if(!oldParentCategory.getId().equals(newParentCategory.getId()))
							{
								oldParentCategory.getCategoryProducts().remove(prod);
							}
							
							break;
						}
					}
					if(oldParentCategory.getId().equals(newParentCategory.getId()))
					{
						catRepo.updateCategoryRepo(oldParentCategory.getId(), oldParentCategory.getName(), oldParentCategory.getImage(), oldParentCategory.getParentCategory(), oldParentCategory.getSubCategories(), oldParentCategory.getCategoryProducts());
					}
					else
					{
						catRepo.updateCategoryRepo(oldParentCategory.getId(), oldParentCategory.getName(), oldParentCategory.getImage(), oldParentCategory.getParentCategory(), oldParentCategory.getSubCategories(), oldParentCategory.getCategoryProducts());
						catRepo.updateCategoryRepo(newParentCategory.getId(), newParentCategory.getName(), newParentCategory.getImage(), newParentCategory.getParentCategory(), newParentCategory.getSubCategories(), newParentCategory.getCategoryProducts());
					}
				}
				else
				{
					if(newBrandId != null)
					{
						Brands brand = brandRepo.getBrandRepo(newBrandId);
					    Pobject productObject = new Pobject(productId, newProductName, newProductImage, brand.getName(), newProductPrice, newProductDiscount);
					    
					    if(newParentCategory.getCategoryProducts() == null)
				    	{
				    		List<Pobject> parentProducts = new ArrayList<>();
				    		parentProducts.add(productObject);
				    		newParentCategory.setCategoryProducts(parentProducts);
				    	}
				    	else
				    		newParentCategory.getCategoryProducts().add(productObject);
					    
					    catRepo.updateCategoryRepo(newParentCategory.getId(), newParentCategory.getName(), newParentCategory.getImage(), newParentCategory.getParentCategory(), newParentCategory.getSubCategories(), newParentCategory.getCategoryProducts());
					}
					else
					{
						Pobject productObject = new Pobject(productId, newProductName, newProductImage, "", newProductPrice, newProductDiscount);
					    
					    if(newParentCategory.getCategoryProducts() == null)
				    	{
				    		List<Pobject> parentProducts = new ArrayList<>();
				    		parentProducts.add(productObject);
				    		newParentCategory.setCategoryProducts(parentProducts);
				    	}
				    	else
				    		newParentCategory.getCategoryProducts().add(productObject);
					    
					    catRepo.updateCategoryRepo(newParentCategory.getId(), newParentCategory.getName(), newParentCategory.getImage(), newParentCategory.getParentCategory(), newParentCategory.getSubCategories(), newParentCategory.getCategoryProducts());
					}
				}
			}
			else
				newParentCategoryId = product.getParentCategory();
			
			prodRepo.updateProductRepo(productId, newProductName, newProductImage, newProductPrice, newProductDiscount, newProductDescription, newBrandId, newParentCategoryId);
			return("Success");
		}
		return("Failed");
	}
}
