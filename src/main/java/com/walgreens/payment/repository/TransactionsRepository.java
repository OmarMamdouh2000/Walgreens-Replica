package com.walgreens.payment.repository;

import com.walgreens.payment.model.TransactionsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TransactionsRepository extends JpaRepository<TransactionsDto, UUID> {

    @Procedure(name = "create_a_transaction")
    void create_a_transaction(
            @Param("p_transaction_uuid") UUID transactionUuid,
            @Param("p_customer_uuid") UUID customerUuid,
            @Param("p_cart_uuid") UUID cartUuid,
            @Param("p_payment_method_uuid") UUID paymentMethodUuid,
            @Param("p_session_id") String sessionId,
            @Param("p_transaction_time") Timestamp transactionTime,
            @Param("p_amount") double amount
            );




}
