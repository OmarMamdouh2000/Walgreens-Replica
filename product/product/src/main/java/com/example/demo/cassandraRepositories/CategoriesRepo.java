package com.example.demo.cassandraRepositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.cassandraModels.Categories;
import com.example.demo.cassandraModels.Pobject;

public interface CategoriesRepo extends CassandraRepository<Categories,UUID> {
	
	@Query("Select * from categories")
	List<Categories> listCategoriesRepo();
	
	@Query("SELECT * FROM categories WHERE id=:categoryId ALLOW FILTERING")
	Categories getCategoryRepo(UUID categoryId);
	
	@Query("DELETE FROM categories WHERE id=:categoryId")
	void deleteCategoryRepo(UUID categoryId);
	
	@Query("INSERT INTO categories (id,name,image,parentcategory,subcategories,categoryproducts) VALUES (:categoryId,:categoryName,:categoryImage,:parentCategory,:categorySubCategories,:categoryProducts)")
	void addCategoryRepo(UUID categoryId, String categoryName, String categoryImage, UUID parentCategory, List<UUID> categorySubCategories, List<Pobject> categoryProducts);

	@Query("UPDATE categories SET name=:categoryName,image=:categoryImage,parentcategory=:parentCategory,subcategories=:categorySubCategories,categoryproducts=:categoryProducts WHERE id=:categoryId")
	void updateCategoryRepo(@Param("categoryId") UUID categoryId, String categoryName, String categoryImage, UUID parentCategory, List<UUID> categorySubCategories, List<Pobject> categoryProducts);
}
