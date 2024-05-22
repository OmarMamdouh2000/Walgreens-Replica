package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.CustomerApplyCouponCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class CustomerApplyCouponProcessor extends Processor{
    @Override
    public void process() {
        CustomerApplyCouponCommand customerApplyCouponCommand = (CustomerApplyCouponCommand) getCommand();
        Map<String, Object> message = getMessageInfo();
        customerApplyCouponCommand.setCustomerUuid(UUID.fromString((String) message.get(Keys.customerUuid)));
        customerApplyCouponCommand.setCouponUuid(UUID.fromString((String) message.get(Keys.couponUuid)));

    }
}
