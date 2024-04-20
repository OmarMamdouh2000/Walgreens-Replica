package com.walgreens.paymentmicroserivce.Models;

import com.walgreens.paymentmicroserivce.Models.Enums.Status;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    @Id

    @SequenceGenerator(name = "transaction_id_sequence",
            sequenceName = "transaction_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_sequence")
    private String transaction_id;
    private String account_id;
    private String payment_method_id;
    private String order_id;
    private Date transaction_date;
    private double amount;
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

    @Override
    public int hashCode() {
        return Objects.hash(transaction_id, account_id, payment_method_id, order_id, transaction_date, amount, status, card_money, wallet_money);
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "transaction_id='" + transaction_id + '\'' +
                ", account_id='" + account_id + '\'' +
                ", payment_method_id='" + payment_method_id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", transaction_date=" + transaction_date +
                ", amount=" + amount +
                ", status=" + status +
                ", card_money=" + card_money +
                ", wallet_money=" + wallet_money +
                '}';
    }
}
