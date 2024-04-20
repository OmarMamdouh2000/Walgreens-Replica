package com.walgreens.paymentmicroserivce.Models;
import com.walgreens.paymentmicroserivce.Models.Enums.card_type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

}
