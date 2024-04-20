package com.walgreens.paymentmicroserivce.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    private String coupon_id;
    private String code;
    private int discount_value;
    private double maximum_discount;
    private double minimum_spend;

}
