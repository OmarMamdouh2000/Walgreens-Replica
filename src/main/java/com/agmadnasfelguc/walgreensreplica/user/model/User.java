package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Role;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "\"User\"")
public class User {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"Role\"", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "\"Status\"", nullable = false)
    private Status status;

    @Column(name = "2FA_Enabled", nullable = false)
    private boolean twoFAEnabled = false;

}
