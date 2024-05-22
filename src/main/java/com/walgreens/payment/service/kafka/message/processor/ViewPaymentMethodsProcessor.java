package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.ViewLoyaltyPointsCommand;
import com.walgreens.payment.service.command.ViewPaymentMethodsCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

;

public class ViewPaymentMethodsProcessor extends Processor{

    @Override
    public void process() {
        ViewPaymentMethodsCommand viewPaymentMethodsCommand = (ViewPaymentMethodsCommand) getCommand();
        Map<String, Object> message = getMessageInfo();

        viewPaymentMethodsCommand.setCustomerUuid(UUID.fromString((String) message.get(Keys.customerUuid)));
    }
}
