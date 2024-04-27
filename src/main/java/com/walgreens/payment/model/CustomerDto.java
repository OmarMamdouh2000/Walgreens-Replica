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
        name = "customers"
)
public class CustomerDto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customer_uuid;


    private String customer_id;


    private int loyalty_points;


}
