package com.walgreens.paymentmicroserivce.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @Column(name = "user_id")
    private String userID;

    @OneToOne(mappedBy = "user")
    private Accounts userAccount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_payment_method_id", referencedColumnName = "user_id")
    private List<PaymentMethods> paymentMethods;
}
