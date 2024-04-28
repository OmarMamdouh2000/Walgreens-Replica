package com.example.demo.cassandraModels;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categories {

	@PrimaryKey
	private UUID id;
	private String name;
	private String image;
	private UUID parentCategory;
	private List<UUID> subCategories;
	private List<Pobject> categoryProducts;

	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public UUID getParentCategory() {
		return parentCategory;
	}
	public void setParentCategory(UUID parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<UUID> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<UUID> subCategories) {
		this.subCategories = subCategories;
	}

	public List<Pobject> getCategoryProducts() {
		return categoryProducts;
	}
	public void setCategoryProducts(List<Pobject> categoryProducts) {
		this.categoryProducts = categoryProducts;
	}
}