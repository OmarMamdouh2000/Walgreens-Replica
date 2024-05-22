package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.CreateCustomerCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class CreateCustomerProcessor extends Processor{
    @Override
    public void process() {
        CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand) getCommand();
        Map<String, Object> message = getMessageInfo();
        System.out.println(message);
        createCustomerCommand.setCustomerUuid(UUID.fromString((String) message.get(Keys.customerUuid)));
        createCustomerCommand.setName((String) message.get(Keys.customerName));
        createCustomerCommand.setEmail((String) message.get(Keys.customerEmail));

    }
}
