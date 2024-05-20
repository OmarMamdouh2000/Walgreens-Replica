package com.walgreens.payment.service.command;

import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.param.CouponCreateParams;
import com.walgreens.payment.controller.PaymentController;
import com.walgreens.payment.model.Enums.Duration;
import com.walgreens.payment.repository.CouponRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CreateCouponCommand implements Command{

    private UUID couponUuid;
    private String name;
    private BigDecimal percentOff;
    private Duration duration;
    private Long duration_in_months;

    Logger logger= LoggerFactory.getLogger(CreateCouponCommand.class);

    @Autowired
    CouponRepository couponRepository;

    @Override
    public void execute() {
        if(duration != Duration.REPEATING){
            duration_in_months = 0L;
        }
        couponUuid = UUID.randomUUID();

        try {
            CouponCreateParams couponCreateParams =
                    CouponCreateParams.builder()
                            .setName(name)
                            .setCurrency("usd")
                            .setDuration(CouponCreateParams.Duration.valueOf(String.valueOf(duration)))
                            .setPercentOff(percentOff)
                            .build();

            Coupon coupon = Coupon.create(couponCreateParams);

            couponRepository.create_coupon(couponUuid, coupon.getId());

            logger.info("Coupon Created");

        } catch (StripeException e) {
            logger.error("Exception createCouponCommand", e);

        }
    }
}
