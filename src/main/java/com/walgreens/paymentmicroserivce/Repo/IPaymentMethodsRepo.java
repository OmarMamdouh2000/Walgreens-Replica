package com.walgreens.paymentmicroserivce.Repo;

import com.walgreens.paymentmicroserivce.Models.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentMethodsRepo extends JpaRepository<PaymentMethods,Integer> {

    @Procedure
}
