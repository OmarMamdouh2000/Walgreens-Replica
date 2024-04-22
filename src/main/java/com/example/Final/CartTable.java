package com.example.Final;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("cart")
public class CartTable {

    @PrimaryKey
    private UUID id;

    @Column("user_id")
    private UUID userId;

    private List<CartItem> items;

    
    private List<CartItem> savedForLaterItems;

    @Column("total_amount")
    private double totalAmount;

    @Column("appliedPromoCodeId")
    private UUID appliedPromoCodeId;

    @Column("orderType")
    private String orderType;

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
        return savedForLaterItems;
    }

    public void setSavedForLaterItems(List<CartItem> savedForLaterItems) {
        this.savedForLaterItems = savedForLaterItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public UUID getAppliedPromoCodeId() {
        return appliedPromoCodeId;
    }

    public void setAppliedPromoCodeId(UUID appliedPromoCodeId) {
        this.appliedPromoCodeId = appliedPromoCodeId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
