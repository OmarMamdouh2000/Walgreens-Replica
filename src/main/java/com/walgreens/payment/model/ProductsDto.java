package com.walgreens.payment.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductsDto {
    private String productName;
    private double productPrice;
    private int productQuantity;
}
