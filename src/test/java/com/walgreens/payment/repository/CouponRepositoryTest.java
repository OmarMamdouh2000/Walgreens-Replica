package com.walgreens.payment.repository;

import com.walgreens.payment.model.Coupon;
import com.walgreens.payment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;

    @Test
    void saveMethod(){
        // Create User
        Coupon coupon = new Coupon();
        coupon.setCode("NEWYEAR");
        coupon.setDiscountValue(33);
        coupon.setMaximumDiscount(33.1);
        coupon.setMinimumSpend(4910);

        //Save User
        Coupon savedcoupon = couponRepository.save(coupon);

        //Display user info
        System.out.println(savedcoupon.toString());
    }

}
