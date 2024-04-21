package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Role;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Table(name = "User")
@Entity
@Data
@Builder
public class User {
    @Id
    private String id;
    @Column
    private String email;
    @Column
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "2FA_Enabled")
    private boolean twoFactorEnabled;
}
