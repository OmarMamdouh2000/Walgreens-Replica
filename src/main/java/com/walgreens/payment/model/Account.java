package com.walgreens.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_unique_constraint",
                        columnNames = "fk_user_id"
                )
        }
)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID account_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_id")
    private User user;

    private double balance;
    private int loyalty_points;


}
