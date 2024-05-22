package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.ViewBalanceCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class ViewBalanceProcessor extends Processor{

    @Override
    public void process() {
        ViewBalanceCommand viewBalanceCommand = (ViewBalanceCommand) getCommand();
        Map<String, Object> message = getMessageInfo();

        viewBalanceCommand.setCustomerUuid(UUID.fromString((String) message.get(Keys.customerUuid)));
    }
}
