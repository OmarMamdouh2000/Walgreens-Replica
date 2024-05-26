package com.example.Final;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

@Table("cart")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartTable {

    @PrimaryKey
    private UUID id;

    @Column("user_id")
    private UUID userId;

    private List<CartItem> items;

    
    private List<CartItem> savedforlateritems;

    @Column("total_amount")
    private double total_amount;

    @Column("appliedpromocodeid")
    private String appliedPromoCodeId;
    
    private double promoCodeAmount;


    public CartTable(){

    }

    public CartTable(UUID id,String appliedPromoCodeId, List<CartItem> items,
                     List<CartItem> savedForLaterItems, double totalAmount, UUID userId)
    {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.savedforlateritems = savedForLaterItems;
        this.total_amount = totalAmount;
        this.appliedPromoCodeId = appliedPromoCodeId;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public List<CartItem> getSavedForLaterItems() {
        return savedforlateritems;
    }

    public void setSavedForLaterItems(List<CartItem> savedForLaterItems) {
        this.savedforlateritems = savedForLaterItems;
    }

    public double getTotalAmount() {
        return total_amount;
    }

    public void setTotalAmount(double totalAmount) {
        this.total_amount = totalAmount;
    }

    public String getAppliedPromoCodeId() {
        return appliedPromoCodeId;
    }

    public void setAppliedPromoCodeId(String appliedPromoCodeId) {
        this.appliedPromoCodeId = appliedPromoCodeId;
    }

//    public double getPromocodeamount() { return promocodeamount;}
//
//    public void setPromocodeamount(double promocodeamount) {this.promocodeamount = promocodeamount; }

  

    @Override
    public String toString() {
        return "CartTable{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", savedForLaterItems=" + savedforlateritems +
                ", totalAmount=" + total_amount +
                ", appliedPromoCodeId=" + appliedPromoCodeId +
                '}';
    }

	public double getPromoCodeAmount() {
		return promoCodeAmount;
	}

	public void setPromoCodeAmount(double promoCodeAmount) {
		this.promoCodeAmount = promoCodeAmount;
	}
    
}
