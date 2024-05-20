package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.CreateCustomerCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class CreateCustomerProcessor extends Processor{
    @Override
    public void process() {
        CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand) getCommand();
        Map<String, String> message = getMessageInfo();
        System.out.println(message);
        createCustomerCommand.setCustomerUuid(UUID.fromString(message.get(Keys.customerUuid)));
        createCustomerCommand.setName(message.get(Keys.customerName));
        createCustomerCommand.setEmail(message.get(Keys.customerEmail));

    }
}
