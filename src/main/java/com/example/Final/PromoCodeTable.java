package com.example.Final;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.Date;

@Table("PromoCodes")
public class PromoCodeTable {

    @PrimaryKey
    private String code;

    @Column("discountvalue")
    private double discountValue;

    private boolean valid;

    @Column("expirydate")
    private LocalDate expiryDate;

    // Getters and setters
    public PromoCodeTable() {
    }

    public PromoCodeTable(String code, double discountValue, boolean valid, LocalDate expiryDate) {
//        this.id = id;
        this.code = code;
        this.discountValue = discountValue;
        this.valid = valid;
        this.expiryDate = expiryDate;
    }

    @Override
    public String toString() {
        return "PromoCode{" +
                ", code='" + code + '\'' +
                ", discountValue=" + discountValue +
                ", valid=" + valid +
                ", expiryDate=" + expiryDate +
                '}';
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
