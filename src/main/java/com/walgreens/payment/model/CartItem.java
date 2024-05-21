package com.walgreens.payment.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    String itemName;
    int itemCount;
    double purchasedPrice;
}
