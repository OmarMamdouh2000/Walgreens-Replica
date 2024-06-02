package com.walgreens.payment.repository;

import com.walgreens.payment.model.Coupon;
import com.walgreens.payment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CouponRepositoryTest {
    @Autowired
    private CouponRepository couponRepository;

    @Test
    void saveMethod(){
        // Create Coupon
        Coupon coupon = new Coupon();
        coupon.setCode("NEWYEAR");
        coupon.setDiscountValue(33);
        coupon.setMaximumDiscount(33.1);
        coupon.setMinimumSpend(4910);

        //Save Coupon
        Coupon savedcoupon = couponRepository.save(coupon);

        //Display Coupon info
        System.out.println(savedcoupon.toString());
    }
    @Test
    void deleteMethod(){
        //Delete Coupon
        couponRepository.delete_coupon(UUID.fromString("25bcc53b-4d58-4e42-8eb9-fda394530a87"));
    }



}
