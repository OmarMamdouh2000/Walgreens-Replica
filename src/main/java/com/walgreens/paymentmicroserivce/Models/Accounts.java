package com.walgreens.paymentmicroserivce.Models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class Accounts {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")

    private String account_id;

    private String user_id;
    private double balance;
    private int loyalty_points;

    public Accounts(String account_id,
                    String user_id,
                    double balance,
                    int loyalty_points) {
        this.account_id = account_id;
        this.user_id = user_id;
        this.balance = balance;
        this.loyalty_points = loyalty_points;
    }

    public Accounts() {

    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getLoyalty_points() {
        return loyalty_points;
    }

    public void setLoyalty_points(int loyalty_points) {
        this.loyalty_points = loyalty_points;
    }
}
