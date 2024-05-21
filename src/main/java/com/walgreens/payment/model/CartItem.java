package com.walgreens.payment.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    @JsonProperty("item_name")
    String itemName;

    @JsonProperty("item_count")
    int itemCount;

    @JsonProperty("purchased_price")
    double purchasedPrice;

    @JsonProperty("deliveryType")
    String deliveryType;

    @JsonProperty("comment")
    String comment;
}
