package com.walgreens.payment.model;

import com.walgreens.payment.model.Enums.Status;

import java.time.LocalDateTime;
import java.util.Date;
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
public class Transactions {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return Double.compare(amount, that.amount) == 0 && Double.compare(card_money, that.card_money) == 0 && Double.compare(wallet_money, that.wallet_money) == 0 && Objects.equals(transaction_id, that.transaction_id) && Objects.equals(account_id, that.account_id) && Objects.equals(payment_method_id, that.payment_method_id) && Objects.equals(order_id, that.order_id) && Objects.equals(transaction_date, that.transaction_date) && status == that.status;
    }


}
