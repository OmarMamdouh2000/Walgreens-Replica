package com.walgreens.paymentmicroserivce.PaymentCommands.ConcreteCommands;

import com.walgreens.paymentmicroserivce.PaymentCommands.Command;

public class ViewPaymentMethodsCommand implements Command {
    private String userId;

    public ViewPaymentMethodsCommand(String userId) {
        this.userId = userId;
    }

    @Override
    public void execute() {

    }


}
