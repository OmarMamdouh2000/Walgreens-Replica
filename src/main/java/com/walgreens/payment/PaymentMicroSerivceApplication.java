package com.walgreens.payment;

import com.walgreens.payment.service.AccountService;
import com.walgreens.payment.service.command.CreateAccountCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class PaymentMicroSerivceApplication implements CommandLineRunner {


    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(PaymentMicroSerivceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        accountService.createAccount(UUID.fromString("d2d09b2a-1b5a-4d4d-b7c1-30d3ce0aa055"));

//        CreateAccountCommand command = CreateAccountCommand.builder().userId(UUID.fromString("123")).accountRepository().build();
    }
}
