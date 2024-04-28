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
    private String productType;
    private ProductSize sizeList;
    private String customMessage;
    private boolean inStore;
    private boolean sameDayDelivery;
    private boolean shipping;
    private int inventory;
    private int pricePerUnit;
    private String ingredients;
    private String warnings;
    private String frequentlyBoughtWith;
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

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public UUID getBrand() {
		return brand;
	}

	public void setBrand(UUID brand) {
		this.brand = brand;
	}

	public ProductSize getSizeList() {
		return sizeList;
	}

	public void setSizeList(ProductSize sizeList) {
		this.sizeList = sizeList;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public boolean isInStore() {
		return inStore;
	}

	public void setInStore(boolean inStore) {
		this.inStore = inStore;
	}

	public boolean isSameDayDelivery() {
		return sameDayDelivery;
	}

	public void setSameDayDelivery(boolean sameDayDelivery) {
		this.sameDayDelivery = sameDayDelivery;
	}

	public boolean isShipping() {
		return shipping;
	}

	public void setShipping(boolean shipping) {
		this.shipping = shipping;
	}

	public int getInventory() {
		return inventory;
	}

	public void setInventory(int inventory) {
		this.inventory = inventory;
	}

	public int getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(int pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getFrequentlyBoughtWith() {
		return frequentlyBoughtWith;
	}

	public void setFrequentlyBoughtWith(String frequentlyBoughtWith) {
		this.frequentlyBoughtWith = frequentlyBoughtWith;
	}

	public boolean isRefundable() {
		return refundable;
	}

	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}

}
