package com.walgreens.payment.service.command;

import com.stripe.exception.StripeException;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Discount;
import com.stripe.param.CustomerCreateParams;
import com.walgreens.payment.repository.CouponRepository;
import com.walgreens.payment.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerApplyCouponCommand implements Command {

    private UUID customerUuid;
    private UUID couponUuid;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public void execute() {

//        try {
//
//
//            String customerId = customerRepository.get_customer(customerUuid);
//            String couponId = couponRepository.get_coupon(couponUuid);
//
//            Customer customer = Customer.retrieve(customerId);
//            Coupon coupon = Coupon.retrieve(couponId);
//
//            customer.setDiscount(Disc);
//            customer.setDiscount(Discount);
//        } catch (StripeException e) {
//            log.error("Exception customerApplyCouponCommand", e);
//        }

    }
}
