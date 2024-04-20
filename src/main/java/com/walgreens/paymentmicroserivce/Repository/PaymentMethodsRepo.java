package com.walgreens.paymentmicroserivce.Repository;

import com.walgreens.paymentmicroserivce.Models.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodsRepo extends JpaRepository<PaymentMethods,Integer> {

    
}
