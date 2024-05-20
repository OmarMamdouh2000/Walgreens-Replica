package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.model.Enums.Duration;
import com.walgreens.payment.service.command.CreateCouponCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.math.BigDecimal;
import java.util.Map;

public class CreateCouponProcessor extends Processor {

    @Override
    public void process() {

        CreateCouponCommand createCouponCommand = (CreateCouponCommand) getCommand();
        Map<String, String> message = getMessageInfo();
        createCouponCommand.setName(message.get(Keys.couponName));
        BigDecimal bigDecimal = new BigDecimal(message.get(Keys.percentOff));
        createCouponCommand.setPercentOff(bigDecimal);
        createCouponCommand.setDuration(Duration.valueOf(message.get(Keys.duration)));
        createCouponCommand.setDuration_in_months(Long.valueOf(message.get(Keys.duration_in_months)));

    }
}
