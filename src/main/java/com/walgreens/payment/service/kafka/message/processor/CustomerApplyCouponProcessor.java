package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.CustomerApplyCouponCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class CustomerApplyCouponProcessor extends Processor{
    @Override
    public void process() {
        CustomerApplyCouponCommand customerApplyCouponCommand = (CustomerApplyCouponCommand) getCommand();
        Map<String, String> message = getMessageInfo().get(Keys.body);
        customerApplyCouponCommand.setCustomerUuid(UUID.fromString(message.get(Keys.customerUuid)));
        customerApplyCouponCommand.setCouponUuid(UUID.fromString(message.get(Keys.couponUuid)));

    }
}
