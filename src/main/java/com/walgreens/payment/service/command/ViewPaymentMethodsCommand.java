package com.walgreens.payment.service.command;

import com.walgreens.payment.service.AccountService;

public class ViewPaymentMethodsCommand implements Command {
    private String userId;

    public ViewPaymentMethodsCommand(String userId) {
        this.userId = userId;
    }

    @Override
    public void execute() {

    }


}
