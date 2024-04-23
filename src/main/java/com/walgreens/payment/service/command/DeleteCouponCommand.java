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
public class DeleteCouponCommand implements Command {

    private UUID couponId;

    @Autowired
    CouponRepository couponRepository;

    @Override
    public void execute() {
        couponRepository.delete_coupon(couponId);
    }

}
