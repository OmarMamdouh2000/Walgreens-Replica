package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.AddPaymentMethodCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class AddPaymentMethodProcessor extends Processor{
    @Override
    public void process() {
        AddPaymentMethodCommand addPaymentMethodCommand = (AddPaymentMethodCommand) getCommand();
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        addPaymentMethodCommand.setCustomerUuid(UUID.fromString(messageInfo.get(Keys.customerUuid)));
        addPaymentMethodCommand.setCardNumber(messageInfo.get(Keys.cardNumber));
        addPaymentMethodCommand.setExpiryMonth(messageInfo.get(Keys.expiryMonth));
        addPaymentMethodCommand.setExpiryYear(messageInfo.get(Keys.expiryYear));
        addPaymentMethodCommand.setCvv(messageInfo.get(Keys.cvv));
        addPaymentMethodCommand.setCardholderName(messageInfo.get(Keys.cardholderName));
        addPaymentMethodCommand.setIsDefault(Boolean.valueOf(messageInfo.get(Keys.isDefault)));


    }
}
