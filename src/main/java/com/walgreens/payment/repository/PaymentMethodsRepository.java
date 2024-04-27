package com.walgreens.payment.repository;

import com.walgreens.payment.model.PaymentMethodsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentMethodsRepository extends JpaRepository<PaymentMethodsDto, UUID> {

    @Procedure(name = "get_customer")
    String get_customer(@Param("p_customer_uuid") UUID customerUuid);

    @Procedure(name = "create_payment_method")
    void create_payment_method(
            @Param("p_payment_method_uuid") UUID paymentMethodUuid,
            @Param("p_customer_uuid") UUID customerUuid,
            @Param("p_card_number") String cardNumber,
            @Param("p_expiry_month") String expiryMonth,
            @Param("p_expiry_year") String expiryYear,
            @Param("p_cvv") String cvv,
            @Param("p_cardholder_name") String cardholderName,
            @Param("p_is_default") Boolean isDefault);


    
}
