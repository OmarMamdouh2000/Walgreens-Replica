package com.example.demo.cassandraRepositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

import com.example.demo.cassandraModels.ProductSize;
import com.example.demo.cassandraModels.Products;


public interface ProductsRepo extends CassandraRepository<Products,UUID> {
	Slice<Products> findAll(Pageable pageable);

	@Query("SELECT * FROM products")
	List<Products> listProductsRepo();
	
	
	@Query("SELECT * FROM products WHERE id=:productId ALLOW FILTERING")
	Products getProductRepo(@Param("productId") UUID productId);
	
   @Query("INSERT INTO products (id, name, description, price, discount, sale, image, product_type, brand_id, size_list, custom_message, in_store, same_day_delivery, shipping, inventory, price_per_unit, ingredients, warnings, frequently_bought_with, refundable) " +
           "VALUES (:id, :name, :description, :price, :discount, :sale, :image, :productType, :brandId, :sizeList, :customMessage, :inStore, :sameDayDelivery, :shipping, :inventory, :pricePerUnit, :ingredients, :warnings, :frequentlyBoughtWith, :refundable)")
    void addProduct(@Param("id") UUID id, @Param("name") String name, @Param("description") String description, 
                    @Param("price") int price, @Param("discount") int discount, @Param("sale") boolean sale, 
                    @Param("image") String image, @Param("productType") String productType, @Param("brandId") UUID brandId, 
                    @Param("sizeList") ProductSize sizeList, @Param("customMessage") String customMessage, 
                    @Param("inStore") boolean inStore, @Param("sameDayDelivery") boolean sameDayDelivery, 
                    @Param("shipping") boolean shipping, @Param("inventory") int inventory, 
                    @Param("pricePerUnit") int pricePerUnit, @Param("ingredients") String ingredients, 
                    @Param("warnings") String warnings, @Param("frequentlyBoughtWith") String frequentlyBoughtWith, 
                    @Param("refundable") boolean refundable);
   
   @Query("UPDATE products SET name=:name, description=:description, price=:price, discount=:discount, sale=:sale, image=:image, product_type=:productType, brand_id=:brandId, size_list=:sizeList, custom_message=:customMessage, in_store=:inStore, same_day_delivery=:sameDayDelivery, shipping=:shipping, inventory=:inventory, price_per_unit=:pricePerUnit, ingredients=:ingredients, warnings=:warnings, frequently_bought_with=:frequentlyBoughtWith, refundable=:refundable WHERE id=:id")
   void updateProduct(@Param("id") UUID id, @Param("name") String name, @Param("description") String description, 
                      @Param("price") int price, @Param("discount") int discount, @Param("sale") boolean sale, 
                      @Param("image") String image, @Param("productType") String productType, @Param("brandId") UUID brandId, 
                      @Param("sizeList") ProductSize sizeList, @Param("customMessage") String customMessage, 
                      @Param("inStore") boolean inStore, @Param("sameDayDelivery") boolean sameDayDelivery, 
                      @Param("shipping") boolean shipping, @Param("inventory") int inventory, 
                      @Param("pricePerUnit") int pricePerUnit, @Param("ingredients") String ingredients, 
                      @Param("warnings") String warnings, @Param("frequentlyBoughtWith") String frequentlyBoughtWith, 
                      @Param("refundable") boolean refundable);
   
   
   @Query("DELETE FROM products WHERE id=:productId")
   void deleteProduct(@Param("productId") UUID productId);
	

}
