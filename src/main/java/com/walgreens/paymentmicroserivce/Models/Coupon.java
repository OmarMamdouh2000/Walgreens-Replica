package com.walgreens.paymentmicroserivce.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Coupon {
    @Id
    private String coupon_id;
    private String code;
    private int discount_value;
    private double maximum_discount;
    private double minimum_spend;

    public Coupon(String coupon_id,
                  String code,
                  int discount_value,
                  double maximum_discount,
                  double minimum_spend) {
        this.coupon_id = coupon_id;
        this.code = code;
        this.discount_value = discount_value;
        this.maximum_discount = maximum_discount;
        this.minimum_spend = minimum_spend;
    }

    public Coupon() {

    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount_value() {
        return discount_value;
    }

    public void setDiscount_value(int discount_value) {
        this.discount_value = discount_value;
    }

    public double getMaximum_discount() {
        return maximum_discount;
    }

    public void setMaximum_discount(double maximum_discount) {
        this.maximum_discount = maximum_discount;
    }

    public double getMinimum_spend() {
        return minimum_spend;
    }

    public void setMinimum_spend(double minimum_spend) {
        this.minimum_spend = minimum_spend;
    }
}
