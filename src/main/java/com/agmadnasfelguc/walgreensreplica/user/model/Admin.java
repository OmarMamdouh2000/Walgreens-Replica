package com.agmadnasfelguc.walgreensreplica.user.model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Entity
@Data
@Table(name = "\"Administrator\"")
public class Admin {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
}
