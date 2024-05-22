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

    

    private UUID itemId;

    private int itemCount;


    private String itemName;


    private double purchasedPrice;

    private String deliveryType;

    private String comment;
}
