package com.walgreens.paymentmicroserivce.Models;
import com.walgreens.paymentmicroserivce.Models.Enums.card_type;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class PaymentMethods {
    @Id
    @SequenceGenerator(name = "payment_method_id_sequence", sequenceName = "payment_method_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_id_sequence")
    private String payment_method_id ;
    private String user_id;
    private card_type card_type;
    private String card_number;
    private String card_holder_name;
    private Date expiry_date;
    private String cvv;
    private Boolean is_default;

    public PaymentMethods(String payment_method_id,
                          String user_id,
                          card_type card_type,
                          String card_number, String card_holder_name,
                          Date expiry_date,
                          String cvv,
                          Boolean is_default) {
        this.payment_method_id = payment_method_id;
        this.user_id = user_id;
        this.card_type = card_type;
        this.card_number = card_number;
        this.card_holder_name = card_holder_name;
        this.expiry_date = expiry_date;
        this.cvv = cvv;
        this.is_default = is_default;
    }

    public PaymentMethods() {

    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public com.walgreens.paymentmicroserivce.Models.Enums.card_type getCard_type() {
        return card_type;
    }

    public void setCard_type(com.walgreens.paymentmicroserivce.Models.Enums.card_type card_type) {
        this.card_type = card_type;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_holder_name() {
        return card_holder_name;
    }

    public void setCard_holder_name(String card_holder_name) {
        this.card_holder_name = card_holder_name;
    }

    public Date getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(Date expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }
}
