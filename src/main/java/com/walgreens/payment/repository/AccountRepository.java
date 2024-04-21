package com.walgreens.payment.repository;

import com.walgreens.payment.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

//    @Procedure
//    void create_account(UUID user_id);

//    @Procedure(name = "public.create_account")
//    void createAccount(@Param("p_user_id") UUID user_id);

    @Procedure(name = "create_account")
    void create_account(@Param("p_account_id") UUID accountId, @Param("p_user_id") UUID userId);





    // Query Method or Finder Method
//    Account findByAccountId(UUID accountId);

}
