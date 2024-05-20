package com.example.demo.cassandraModels;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;



@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {

	@PrimaryKey
	private UUID id;
	private String name;
	private String image;
	private double price;
	private String discount;
	private String description;
	private UUID brand;
	private UUID parentCategory;
	private boolean sale;
	private int inventory;
	private boolean refundable;

//    public Products(UUID id, String name, String description, int price, String image, UUID parentCategory) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.image= image;
//        this.parentCategory = parentCategory;
//    }

	public UUID getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(UUID parentCategory) {
		this.parentCategory = parentCategory;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public UUID getBrand() {
		return brand;
	}

	public void setBrand(UUID brand) {
		this.brand = brand;
	}

	public int getInventory() {
		return inventory;
	}

	public void setInventory(int inventory) {
		this.inventory = inventory;
	}

	public boolean isRefundable() {
		return refundable;
	}

	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}

}
