package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.CheckPaymentMethodCommand;
import com.walgreens.payment.service.command.CreateCheckoutCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class CheckPaymentMethodProcessor extends Processor{

    @Override
    public void process() {
        CheckPaymentMethodCommand checkPaymentMethodCommand = (CheckPaymentMethodCommand) getCommand();
        Map<String, String> message = getMessageInfo();
        checkPaymentMethodCommand.setCustomerUuid(UUID.fromString(message.get(Keys.customerUuid)));
        checkPaymentMethodCommand.setCartUuid(UUID.fromString(message.get(Keys.cartUuid)));
        checkPaymentMethodCommand.setAmount(Double.parseDouble(message.get(Keys.paymentAmount)));
    }
}
