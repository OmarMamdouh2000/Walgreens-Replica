package com.walgreens.paymentmicroserivce.Models;

import com.walgreens.paymentmicroserivce.Models.Enums.Status;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

@Entity

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



    public Transactions(String transaction_id,
                        String account_id,
                        String payment_method_id,
                        String order_id,
                        Date transaction_date,
                        double amount,
                        Status status,
                        double card_money,
                        double wallet_money) {
        this.transaction_id = transaction_id;
        this.account_id = account_id;
        this.payment_method_id = payment_method_id;
        this.order_id = order_id;
        this.transaction_date = transaction_date;
        this.amount = amount;
        this.status = status;
        this.card_money = card_money;
        this.wallet_money = wallet_money;
    }

    public Transactions() {
    }



    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(Date transaction_date) {
        this.transaction_date = transaction_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getCard_money() {
        return card_money;
    }

    public void setCard_money(double card_money) {
        this.card_money = card_money;
    }

    public double getWallet_money() {
        return wallet_money;
    }

    public void setWallet_money(double wallet_money) {
        this.wallet_money = wallet_money;
    }

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
