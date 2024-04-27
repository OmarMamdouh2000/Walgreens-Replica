package com.walgreens.payment.repository;

import com.walgreens.payment.model.TransactionsDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionsRepository extends JpaRepository<TransactionsDto, UUID> {

//    @Procedure(name = "add_a_transaction")
//    void addATransaction(
//            @Param("p_transaction_id") UUID transaction_id,
//            @Param("p_account_id") UUID account_id,
//            @Param("p_payment_method_id") UUID payment_method_id,
//            @Param("p_order_id") UUID order_id,
//            @Param("p_transaction_date") LocalDateTime transaction_date,
//            @Param("p_amount") double amount,
//            @Param("p_wallet_money") double wallet_money,
//            @Param("p_card_money") double card_money,
//            @Param("p_status") Status status
//            );


}
