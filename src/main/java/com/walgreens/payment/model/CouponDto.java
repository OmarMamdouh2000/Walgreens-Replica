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
@Table(
        name = "coupons"
)
public class CouponDto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID coupon_uuid;

    private String coupon_id;


}
