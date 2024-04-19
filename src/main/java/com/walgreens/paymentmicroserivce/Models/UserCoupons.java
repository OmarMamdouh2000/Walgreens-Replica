package com.walgreens.paymentmicroserivce.Models;

import com.walgreens.paymentmicroserivce.Models.Keys.UserCouponKey;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity @IdClass(UserCouponKey.class)
public class UserCoupons {
    @Id
    private String user_id;
    @Id
    private String coupon_id;


    public UserCoupons(String user_id,
                       String coupon_id) {
        this.user_id = user_id;
        this.coupon_id = coupon_id;
    }

    public UserCoupons() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }
}
