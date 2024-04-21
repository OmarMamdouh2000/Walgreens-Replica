package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Role;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Table(name = "Administrator")
@Entity
@Data
@Builder
@NamedStoredProcedureQuery(name = "LoginAdmin", procedureName = "Login_Admin",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "email", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "password", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "status", type = Status.class),
        })
public class Admin {
    @Id
    private String id;
    @Column
    private String username;
    @Column
    private String password;
}
