package com.walgreens.payment.service.command;

import com.walgreens.payment.repository.AccountRepository;
import com.walgreens.payment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewPaymentMethodsCommand implements Command {
//    private String userId;
//
//    public ViewPaymentMethodsCommand(String userId) {
//        this.userId = userId;
//    }

    @Autowired
    private AccountRepository accountRepository;



    @Override
    public void execute() {

    }


}
