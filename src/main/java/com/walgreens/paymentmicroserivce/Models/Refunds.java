package com.walgreens.paymentmicroserivce.Models;

import com.walgreens.paymentmicroserivce.Models.Enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Refunds {
    @Id
    private String refund_id;
    private String order_id;
    private String user_id;
    private String reason;
    private Status status;
    private double amount;

    public Refunds(String refund_id,
                   String order_id,
                   String user_id,
                   String reason,
                   Status status,
                   double amount) {
        this.refund_id = refund_id;
        this.order_id = order_id;
        this.user_id = user_id;
        this.reason = reason;
        this.status = status;
        this.amount = amount;
    }

    public Refunds() {
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
