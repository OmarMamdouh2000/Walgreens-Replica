package com.walgreens.payment;

import com.walgreens.payment.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.context.annotation.Bean;


import java.util.UUID;

@SpringBootApplication(scanBasePackages = {"com.walgreens.payment.*","org.springdoc"})
public class PaymentMicroSerivceApplication  {


    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(PaymentMicroSerivceApplication.class, args);
    }


}
