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
@NamedStoredProcedureQuery(name = "Login", procedureName = "Login",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "password", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "status", type = Status.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "role", type = Role.class)
        })

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
