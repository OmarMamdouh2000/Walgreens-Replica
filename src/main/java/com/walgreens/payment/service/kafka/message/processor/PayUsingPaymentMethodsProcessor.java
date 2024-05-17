package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.PayUsingPaymentMethodsCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class PayUsingPaymentMethodsProcessor extends Processor{

    @Override
    public void process() {

        PayUsingPaymentMethodsCommand payUsingPaymentMethodsCommand = (PayUsingPaymentMethodsCommand) getCommand();
        Map<String, String> message = getMessageInfo().get(Keys.body);

        payUsingPaymentMethodsCommand.setPaymentMethodUuid(UUID.fromString(message.get(Keys.paymentMethodUuid)));
        payUsingPaymentMethodsCommand.setAmount(Double.valueOf(message.get(Keys.paymentAmount)));
        payUsingPaymentMethodsCommand.setCartUuid(UUID.fromString(message.get(Keys.cartUuid)));

    }
}
