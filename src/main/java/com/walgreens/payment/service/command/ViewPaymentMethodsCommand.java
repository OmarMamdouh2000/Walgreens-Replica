package com.walgreens.payment.service.command;

public class ViewPaymentMethodsCommand implements Command {
    private String userId;

    public ViewPaymentMethodsCommand(String userId) {
        this.userId = userId;
    }

    @Override
    public void execute() {

    }


}
