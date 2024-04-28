package com.example.demo.cassandraRepositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.cassandraModels.Brand;
import com.example.demo.cassandraModels.Pobject;


public interface BrandRepo extends CassandraRepository<Brand,UUID>{
	@Query("Select * from brand") 
    List<Brand> listBrandRepo();
	
	@Query("Select * from Brand where id=:brandId ALLOW FILTERING")
	Brand getBrandRepo(@Param("brandId") UUID brandId);
	
	@Query("DELETE FROM brand WHERE id=:brandId")
	void deleteBrandRepo(@Param("brandId") UUID brandId);
	
	@Query("INSERT INTO brand (id,name,brandproducts) VALUES (:brandId,:brandName,:brandProducts)")
	void addBrandRepo(UUID brandId,String brandName, List<Pobject> brandProducts);
	
	@Query("UPDATE brand SET name=:brandName,brandproducts=:brandProducts WHERE id=:brandId")
	void updateBrandRepo(@Param("brandId") UUID brandId, String brandName, List<Pobject> brandProducts);
}