package com.example.Final;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CartItem {

    @CassandraType(type = CassandraType.Name.UUID)
    private UUID item_id;

    @CassandraType(type = CassandraType.Name.INT)
    private int item_count;

    @CassandraType(type=CassandraType.Name.TEXT)
    private String item_name;
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private double purchased_price;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String deliveryType;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String comment;

    public CartItem() {
    }

    public CartItem(UUID itemId, int itemCount,String item_name, double purchasedPrice, String deliveryType, String comment) {
        this.item_id = itemId;
        this.item_count = itemCount;
        this.purchased_price = purchasedPrice;
        this.deliveryType = deliveryType;
        this.comment = comment;
        this.item_name = item_name;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "itemId=" + item_id +
                ", itemCount=" + item_count +
                ", item_name='" + item_name +
                ", purchasedPrice=" + purchased_price +
                ", deliveryType='" + deliveryType + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }

    // Getters and setters

    public UUID getItemId() {
        return item_id;
    }

    public void setItemId(UUID itemId) {
        this.item_id = itemId;
    }
    public String getItemName() {
        return item_name;
    }
    public void setItemName(String item_name) {
        this.item_name = item_name;
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

