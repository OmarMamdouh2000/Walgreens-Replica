package com.walgreens.paymentmicroserivce.Models;
import com.walgreens.paymentmicroserivce.Models.Enums.card_type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethods {
    @Id
    @SequenceGenerator(name = "payment_method_id_sequence", sequenceName = "payment_method_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_id_sequence")
    private String payment_method_id ;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_user_id")
//    private Users user;

//    private String user_id;
    private card_type card_type;
    private String card_number;
    private String card_holder_name;
    private Date expiry_date;
    private String cvv;
    private Boolean is_default;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethods that = (PaymentMethods) o;
        return Objects.equals(payment_method_id, that.payment_method_id) && card_type == that.card_type && Objects.equals(card_number, that.card_number) && Objects.equals(card_holder_name, that.card_holder_name) && Objects.equals(expiry_date, that.expiry_date) && Objects.equals(cvv, that.cvv) && Objects.equals(is_default, that.is_default);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payment_method_id, card_type, card_number, card_holder_name, expiry_date, cvv, is_default);
    }
}
