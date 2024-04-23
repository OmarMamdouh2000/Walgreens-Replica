package com.walgreens.payment.repository;

import com.walgreens.payment.model.Account;
import com.walgreens.payment.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    @Procedure(name = "create_coupon")
    void create_coupon(@Param("p_coupon_id") UUID couponId,
                        @Param("p_code") String code,
                        @Param("p_discount_value") double discountValue,
                        @Param("p_maximum_discount") double maximumDiscount,
                        @Param("p_minimum_spend") double minimumSpend
                        );

}
