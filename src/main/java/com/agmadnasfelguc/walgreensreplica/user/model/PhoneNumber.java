package com.agmadnasfelguc.walgreensreplica.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
@Table(name = "\"Phone_Number\"")
public class PhoneNumber {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String number;

    @Column(nullable = false)
    private String extension;
}
