package com.walgreens.payment;

import com.walgreens.payment.service.AccountService;
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
        accountService.createAccount(UUID.fromString("7f8df2bd-ab57-4018-8e77-ee704c354ee4"));
    }
}
