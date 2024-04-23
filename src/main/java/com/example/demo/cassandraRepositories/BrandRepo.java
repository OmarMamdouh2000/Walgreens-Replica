package com.example.demo.cassandraRepositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.example.demo.cassandraModels.Brand;


public interface BrandRepo extends CassandraRepository<Brand,UUID>{
	@Query("Select * from brand") 
    List<Brand> listBrandRepo();
	
	@Query("Select * from Brand where id=:id ALLOW FILTERING")
	 Brand getBrandRepo(UUID id);
	
	@Query("DELETE FROM brand WHERE id=:brandID")
	 void deleteBrandRepo(UUID brandID);
	
	@Query("INSERT INTO brand (id,name,categories_id) VALUES (:brandId,:brandName,:categories)")
	void insertBrandRepo(UUID brandId,String brandName, List<UUID> categories);
	
	@Query("UPDATE brand SET name=:brandName WHERE id=:brandId")
	void updateBrandRepo(UUID brandId, String brandName);
}