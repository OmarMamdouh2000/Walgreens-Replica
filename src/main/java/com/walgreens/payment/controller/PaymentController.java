package com.walgreens.payment.controller;

import com.walgreens.payment.service.command.Command;
import com.walgreens.payment.service.command.CreateAccountCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PaymentController implements CommandLineRunner {

    private final Command createAccountCommand;

    @Autowired
    public PaymentController(Command createAccountCommand) {
        this.createAccountCommand = createAccountCommand;
    }

    public void createAccount(UUID userID){

//        Command createAccount = ((CreateAccountCommand)createAccountCommand).builder().userId(userID).build();
        ((CreateAccountCommand)createAccountCommand).setUserId(userID);
        createAccountCommand.execute();
    }


    @Override
    public void run(String... args) throws Exception {
        this.createAccount(UUID.fromString("0e9e047a-da18-474b-a416-8022a82b605e"));
    }
}
