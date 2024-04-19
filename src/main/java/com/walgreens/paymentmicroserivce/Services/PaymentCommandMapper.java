package com.walgreens.paymentmicroserivce.Services;

import com.walgreens.paymentmicroserivce.PaymentCommands.Command;

public class PaymentCommandMapper {
    public Command getCommand(String messageType) {
        if ("type1".equals(requestType)) {
            return new SpecificCommand();
        }
        throw new IllegalArgumentException("Invalid request type");
    }
}
