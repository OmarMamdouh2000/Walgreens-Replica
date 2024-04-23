package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCouponCommand implements Command{

    private String code;
    private int discountValue;
    private double maximumDiscount;
    private double minimumSpend;


    @Autowired
    CouponRepository couponRepository;
    @Override
    public void execute() {
        UUID couponId = UUID.randomUUID();
        couponRepository.create_coupon(couponId,code,discountValue,maximumDiscount,minimumSpend);
    }
}
