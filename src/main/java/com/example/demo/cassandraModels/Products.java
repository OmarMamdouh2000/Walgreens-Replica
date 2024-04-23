package com.example.demo.cassandraModels;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;
import com.datastax.oss.driver.api.core.uuid.Uuids;



@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {

    @PrimaryKey
    private UUID id;
    private String name;
    private String description;
    private int price;
    private int discount;
    private boolean sale;
    private String image;
    private ArrayList<UUID> parentCategories;
    private String productType;
    private UUID brandId;
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

    public Products(String name, String description, int price, String productType) {
        this.id = Uuids.timeBased();
        this.name = name;
        this.description = description;
        this.price = price;
        this.productType = productType;
        this.parentCategories = new ArrayList<>();
    }

	public UUID getId() {
		return id;
	}

//	public void setId(UUID id) {
//		this.id = id;
//	}

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

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public boolean isSale() {
		return sale;
	}

	public void setSale(boolean sale) {
		this.sale = sale;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ArrayList<UUID> getParentCategories() {
		return parentCategories;
	}

	public void setParentCategories(ArrayList<UUID> parentCategories) {
		this.parentCategories = parentCategories;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public UUID getBrandId() {
		return brandId;
	}

	public void setBrandId(UUID brandId) {
		this.brandId = brandId;
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
