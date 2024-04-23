package com.example.demo.cassandraRepositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.example.demo.cassandraModels.Categories;

public interface CategoriesRepo extends CassandraRepository<Categories,UUID> {
	
	@Query("Select * from categories")
	List<Categories> listCategoriesRepo();
	
	@Query("SELECT * FROM categories WHERE id=:categoryId ALLOW FILTERING")
	Categories getCategoryRepo(UUID categoryId);
	
	@Query("DELETE FROM categories WHERE id=:categoryId")
	void deleteCategoryRepo(UUID categoryId);
	
	@Query("INSERT INTO categories (id,name,image,parentcategory,subcategories,products) VALUES (:categoryId,:categoryName,:categoryImage,:parentCategory,:categorySubCategories,:categoryProducts)")
	void addCategoryRepo(UUID categoryId, String categoryName, String categoryImage, UUID parentCategory, List<UUID> categorySubCategories, List<UUID> categoryProducts);

	@Query("UPDATE categories SET name=:categoryName,image=:categoryImage,parentcategory=:parentCategory,subcategories=:categorySubCategories,products=:categoryProducts WHERE id=:categoryId")
	void updateCategoryRepo(UUID categoryId, String categoryName, String categoryImage, UUID parentCategory, List<UUID> categorySubCategories, List<UUID> categoryProducts);
}
