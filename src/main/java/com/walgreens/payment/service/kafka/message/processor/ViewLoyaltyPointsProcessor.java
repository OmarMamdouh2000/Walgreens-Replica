package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.ViewLoyaltyPointsCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class ViewLoyaltyPointsProcessor extends Processor{

    @Override
    public void process() {
        ViewLoyaltyPointsCommand viewLoyaltyPointsCommand = (ViewLoyaltyPointsCommand) getCommand();
        Map<String, Object> message = getMessageInfo();

        viewLoyaltyPointsCommand.setCustomerUuid(UUID.fromString((String) message.get(Keys.customerUuid)));
    }
}
