package com.walgreens.payment.service;

import com.walgreens.payment.repository.AccountRepository;
import com.walgreens.payment.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    public void createAccount(UUID user_id){
        UUID account_id = UUID.randomUUID();
        accountRepository.create_account(account_id, user_id);
    }

//    public void getBalance(UUID account_id){
//        accountRepository.get_balance(account_id);
//        double balance = accountRepository.get_balance(account_id);
//        System.out.println("Balance: " + balance);
//    }
//    public void printBalance(UUID accountId) {
//        double balance = getBalance(accountId);
//        System.out.println("Balance: " + balance);
//    }

    @Autowired
    private EntityManager entityManager;

    public double getBalance(UUID accountId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("get_balance");
        query.registerStoredProcedureParameter("p_account_id", UUID.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_balance", Double.class, ParameterMode.OUT);
        query.setParameter("p_account_id", accountId);
        query.execute();
        return (Double) query.getOutputParameterValue("p_balance");
    }

    public void printBalance(UUID accountId) {
        double balance = getBalance(accountId);
        System.out.println("Balance: " + balance);
    }


}
