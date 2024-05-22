package com.walgreens.payment.service.kafka.message.processor;

import com.walgreens.payment.service.command.AddPaymentMethodCommand;
import com.walgreens.payment.service.kafka.message.keys.Keys;

import java.util.Map;
import java.util.UUID;

public class AddPaymentMethodProcessor extends Processor{
    @Override
    public void process() {
        AddPaymentMethodCommand addPaymentMethodCommand = (AddPaymentMethodCommand) getCommand();
        Map<String, Object> messageInfo = getMessageInfo();
        addPaymentMethodCommand.setCustomerUuid(UUID.fromString((String) messageInfo.get(Keys.customerUuid)));
        addPaymentMethodCommand.setCardNumber((String) messageInfo.get(Keys.cardNumber));
        addPaymentMethodCommand.setExpiryMonth((String) messageInfo.get(Keys.expiryMonth));
        addPaymentMethodCommand.setExpiryYear((String) messageInfo.get(Keys.expiryYear));
        addPaymentMethodCommand.setCvv((String) messageInfo.get(Keys.cvv));
        addPaymentMethodCommand.setCardholderName((String) messageInfo.get(Keys.cardholderName));
        addPaymentMethodCommand.setIsDefault(Boolean.valueOf((Boolean) messageInfo.get(Keys.isDefault)));
        addPaymentMethodCommand.setHasFunds(Boolean.valueOf((Boolean) messageInfo.get(Keys.hasFunds)));



    }
}
