package com.walgreens.payment.model;
import com.walgreens.payment.model.Enums.card_type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID payment_method_id ;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_user_id")
//    private Users user;

//    private String user_id;
    @Enumerated(EnumType.STRING)
//    @Column(name = "card_type")
    private card_type card_type;

    @Column(columnDefinition = "TEXT")
    private String card_number;

    @Column(columnDefinition = "TEXT")
    private String card_holder_name;
    private LocalDate expiry_date;
    @Column(columnDefinition = "TEXT")
    private String cvv;
    private Boolean is_default;


}
