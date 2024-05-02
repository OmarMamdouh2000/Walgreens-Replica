package com.example.demo.cassandraRepositories;

import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.example.demo.cassandraModels.Products;




public interface ProductsRepo extends CassandraRepository<Products,UUID> {
	
	Slice<Products> findAll(Pageable pageable);

//	@Query("SELECT * FROM products")
//	List<Products> listProductsRepo();
	
	@Query("SELECT * FROM products WHERE id=:productId ALLOW FILTERING")
	Products getProductRepo(UUID productId);
	
	@Query("DELETE FROM products WHERE id=:productId")
	void deleteProductRepo(UUID productId);
	
	@Query("INSERT INTO products (id,name,image,price,discount,description,brand,parentcategory) VALUES (:productId,:productName,:productImage,:productPrice,:productDiscount,:productDescription,:brandId,:categoryId)")
	void addProductRepo(UUID productId, String productName, String productImage,  double productPrice, String productDiscount, String productDescription, UUID brandId, UUID categoryId);
	
	@Query("UPDATE products SET name=:productName,image=:productImage,price=:productPrice,discount=:productDiscount,description=:productDescription,brand=:brandId,parentcategory=:categoryId WHERE id=:productId")
	void updateProductRepo(UUID productId, String productName, String productImage,  double productPrice, String productDiscount, String productDescription, UUID brandId, UUID categoryId);

}
