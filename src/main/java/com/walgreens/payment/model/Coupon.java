package com.walgreens.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "coupon_id",
            updatable = false
    )
    private UUID couponId;

    @Column(
            name = "code",
            updatable = false
    )
    private String code;

    @Column(
            name = "discount_value",
            updatable = false
    )
    private int discountValue;

    @Column(
            name = "maximum_discount",
            updatable = false
    )
    private double maximumDiscount;

    @Column(
            name = "minimum_spend",
            updatable = false
    )
    private double minimumSpend;

}
