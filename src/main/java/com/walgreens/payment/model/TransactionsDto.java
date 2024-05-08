package com.walgreens.payment.model;

import com.walgreens.payment.model.Enums.Status;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "transactions"
)
public class TransactionsDto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transaction_uuid;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_customer_uuid", referencedColumnName = "customer_uuid")
    private CustomerDto customer_uuid;


    private UUID cart_uuid;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_payment_method_uuid", referencedColumnName = "payment_method_uuid")
    private PaymentMethodsDto payment_method_id;


    private String session_id;


    @CreationTimestamp
    private LocalDateTime transaction_time;

    private double amount;








}
