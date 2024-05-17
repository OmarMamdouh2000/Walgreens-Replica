package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.CreateCustomerCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;

public class CreateCustomerProcessor extends Processor{
    @Override
    public void process() {
        CreateCustomerCommand createCustomerCommand = (CreateCustomerCommand) getCommand();
        Map<String, String> message = getMessageInfo().get(Keys.body);

        createCustomerCommand.setName(message.get(Keys.customerName));
        createCustomerCommand.setEmail(message.get(Keys.customerEmail));

    }
}
