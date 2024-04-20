package com.walgreens.payment.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID user_id;

    private String firstName;

//    @OneToOne(mappedBy = "user")
//    private Accounts userAccount;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_payment_method_id", referencedColumnName = "user_id")
//    private List<PaymentMethods> paymentMethods;
}
