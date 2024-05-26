package com.agmadnasfelguc.walgreensreplica.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "\"Pharmacist\"")
@Entity
@Data
public class Pharmacist{
    @Id
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
}