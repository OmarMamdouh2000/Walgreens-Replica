package com.walgreens.payment.repository;

import com.walgreens.payment.model.CouponDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CouponRepository extends JpaRepository<CouponDto, UUID> {

    @Procedure(name = "create_coupon")
    void create_coupon (@Param("p_coupon_uuid") UUID couponUuid, @Param("p_coupon_id") String couponID);

    @Procedure(name = "get_coupon")
    String get_coupon(@Param("p_coupon_uuid") UUID couponUuid);

}
