package com.walgreens.payment.model;

import com.walgreens.payment.model.Enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Refunds {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID refund_id;
    private UUID order_id;
    private UUID user_id;
    private String reason;
    private Status status;
    private double amount;


}
