package com.example.demo.cassandraRepositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.example.demo.cassandraModels.Brands;
import com.example.demo.cassandraModels.Pobject;


public interface BrandsRepo extends CassandraRepository<Brands,UUID>{
	@Query("Select * from brands") 
    List<Brands> listBrandRepo();
	
	@Query("Select * from Brands where id=:brandId ALLOW FILTERING")
	Brands getBrandRepo(UUID brandId);
	
	@Query("DELETE FROM brands WHERE id=:brandId")
	void deleteBrandRepo(UUID brandId);
	
	@Query("INSERT INTO brands (id,name,brandproducts) VALUES (:brandId,:brandName,:brandProducts)")
	void addBrandRepo(UUID brandId,String brandName, List<Pobject> brandProducts);
	
	@Query("UPDATE brands SET name=:brandName,brandproducts=:brandProducts WHERE id=:brandId")
	void updateBrandRepo(UUID brandId, String brandName, List<Pobject> brandProducts);
}