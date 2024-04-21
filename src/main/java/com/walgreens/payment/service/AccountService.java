package com.walgreens.payment.service;

import com.walgreens.payment.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void createAccount(UUID user_id){
        accountRepository.createAccount(user_id);
    }
}
