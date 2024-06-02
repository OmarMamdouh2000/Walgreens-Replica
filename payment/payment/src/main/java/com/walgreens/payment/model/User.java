package com.walgreens.payment.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "", columnNames = "email")
//        }
)
public class User {

    @Id
    @Column(
            name = "user_id",
            updatable = false
    )
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID user_id;

    @Column(
            name = "first_name",
            nullable = false,
            columnDefinition = "TEXT"

    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"

    )
    private String lastName;



//    @OneToOne(mappedBy = "user")
//    private Accounts userAccount;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "fk_payment_method_id", referencedColumnName = "user_id")
//    private List<PaymentMethods> paymentMethods;
}
