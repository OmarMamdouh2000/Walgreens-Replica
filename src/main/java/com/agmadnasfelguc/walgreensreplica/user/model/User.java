package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Role;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Status;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@SqlResultSetMapping(
        name = "LoginResultMapping",
        classes = {
                @ConstructorResult(
                        targetClass = LoginResult.class,
                        columns = {
                                @ColumnResult(name = "user_id", type = UUID.class),
                                @ColumnResult(name = "status", type = String.class),
                                @ColumnResult(name = "message", type = String.class),
                                @ColumnResult(name = "role", type = String.class)
                        }
                )
        }
)
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
