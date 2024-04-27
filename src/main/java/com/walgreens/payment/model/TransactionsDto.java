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
    private UUID transaction_id;

    private UUID account_id;
    private UUID payment_method_id;
    private UUID order_id;
    @CreationTimestamp
    private LocalDateTime transaction_date;
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    private double card_money;
    private double wallet_money;




}
