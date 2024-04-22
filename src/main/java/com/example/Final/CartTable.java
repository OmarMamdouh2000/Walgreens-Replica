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
/*

INSERT INTO cart (id, user_id, items, savedforlateritems, total_amount, appliedpromocodeid, ordertype)
VALUES (
    uuid(),
    72678a7f-3922-4882-8a18-8dd0181213c2,
    [
        {item_id: uuid(), item_count: 2, purchased_price: 19.99, deliveryType: 'Standard', comment: 'None'},
        {item_id: uuid(), item_count: 1, purchased_price: 29.99, deliveryType: 'Express', comment: 'Gift'}
    ],
    [],
    89.97,
    uuid(),
    'online'
);

*/
    @PrimaryKey
    private UUID id;

    @Column("user_id")
    private UUID userId;

    private List<CartItem> items;

    
    private List<CartItem> savedforlateritems;

    @Column("total_amount")
    private double total_amount;

    @Column("appliedpromocodeid")
    private UUID appliedpromocodeid;

    @Column("ordertype")
    private String ordertype;

    public CartTable(){

    }

    public CartTable(UUID id,UUID appliedPromoCodeId, List<CartItem> items, String orderType,
                     List<CartItem> savedForLaterItems, double totalAmount, UUID userId)
    {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.savedforlateritems = savedForLaterItems;
        this.total_amount = totalAmount;
        this.appliedpromocodeid = appliedPromoCodeId;
        this.ordertype = orderType;
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

    public UUID getAppliedPromoCodeId() {
        return appliedpromocodeid;
    }

    public void setAppliedPromoCodeId(UUID appliedPromoCodeId) {
        this.appliedpromocodeid = appliedPromoCodeId;
    }

    public String getOrderType() {
        return ordertype;
    }

    public void setOrderType(String orderType) {
        this.ordertype = orderType;
    }

    @Override
    public String toString() {
        return "CartTable{" +
                "id=" + id +
                ", userId=" + userId +
                ", items=" + items +
                ", savedForLaterItems=" + savedforlateritems +
                ", totalAmount=" + total_amount +
                ", appliedPromoCodeId=" + appliedpromocodeid +
                ", orderType='" + ordertype + '\'' +
                '}';
    }
}
