package com.walgreens.paymentmicroserivce.Models;

import com.walgreens.paymentmicroserivce.Models.Enums.Status;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Transactions {
    @Id
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
}
