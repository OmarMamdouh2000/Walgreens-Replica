package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.ViewLoyaltyPointsCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class ViewLoyaltyPointsProcessor extends Processor{

    @Override
    public void process() {
        ViewLoyaltyPointsCommand viewLoyaltyPointsCommand = (ViewLoyaltyPointsCommand) getCommand();
        Map<String, String> message = getMessageInfo().get(Keys.body);

        viewLoyaltyPointsCommand.setCustomerUuid(UUID.fromString(message.get(Keys.customerUuid)));
    }
}
