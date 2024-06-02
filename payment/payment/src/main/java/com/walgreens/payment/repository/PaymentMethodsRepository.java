package com.walgreens.payment.repository;

import com.walgreens.payment.model.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods, UUID> {


    
}
