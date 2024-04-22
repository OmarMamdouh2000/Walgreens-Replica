package com.example.Final;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.UUID;

@UserDefinedType("cartitem")
public class CartItem {

    @CassandraType(type = CassandraType.Name.UUID)
    private UUID item_id;

    @CassandraType(type = CassandraType.Name.INT)
    private int item_count;

    @CassandraType(type = CassandraType.Name.DOUBLE)
    private double purchased_price;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String deliveryType;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String comment;

    // Getters and setters

    public UUID getItemId() {
        return item_id;
    }

    public void setItemId(UUID itemId) {
        this.item_id = itemId;
    }

    public int getItemCount() {
        return item_count;
    }

    public void setItemCount(int itemCount) {
        this.item_count = itemCount;
    }

    public double getPurchasedPrice() {
        return purchased_price;
    }

    public void setPurchasedPrice(double purchasedPrice) {
        this.purchased_price = purchasedPrice;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

