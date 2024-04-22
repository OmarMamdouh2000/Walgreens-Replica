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
      //  accountService.createAccount(UUID.fromString("6a9fb292-b400-48f6-9b4f-8208ca168bce"));

     //   accountService.getBalance(UUID.fromString("f737eeb9-9015-4470-951c-f95f24083a77"));
        UUID accountId = UUID.fromString("f737eeb9-9015-4470-951c-f95f24083a77");
        accountService.printBalance(accountId);
        //  CreateAccountCommand command = CreateAccountCommand.builder().userId(UUID.fromString("123")).accountRepository().build();
    }
}
