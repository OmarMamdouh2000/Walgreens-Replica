package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Role;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Status;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name ="\"TwoFactorAuth_Enabled\"", nullable = false)
    private boolean twoFAEnabled = false;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "image_id")
    private UUID imageId;

}
