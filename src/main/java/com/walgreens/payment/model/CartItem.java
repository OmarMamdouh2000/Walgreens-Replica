package com.walgreens.payment.model;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    
    @JsonProperty("item_id")

    private UUID item_id;
    @JsonProperty("item_count")

    private int item_count;
    @JsonProperty("item_name")

    private String item_name;
    @JsonProperty("purchased_price")

    private double purchased_price;
    @JsonProperty("deliveryType")
    private String deliveryType;
    @JsonProperty("comment")
    private String comment;
}
