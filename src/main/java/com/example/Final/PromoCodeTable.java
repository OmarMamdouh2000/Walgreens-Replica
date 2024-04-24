package com.example.Final;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Table("PromoCodes")
public class PromoCodeTable {

    @PrimaryKeyColumn
    private UUID id;

    @PrimaryKeyColumn
    private String code;

    @Column("discountvalue")
    private double discountValue;

    private boolean valid;

    @Column("expirydate")
    private LocalDate expiryDate;

    // Getters and setters
    public PromoCodeTable() {
    }

    public PromoCodeTable(UUID id, String code, double discountValue, boolean valid, LocalDate expiryDate) {
        this.id = id;
        this.code = code;
        this.discountValue = discountValue;
        this.valid = valid;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "PromoCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", discountValue=" + discountValue +
                ", valid=" + valid +
                ", expiryDate=" + expiryDate +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
