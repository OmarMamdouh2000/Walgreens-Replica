package com.agmadnasfelguc.walgreensreplica.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Table(name = "Administrator")
@Entity
@Data
@Builder
public class Admin {
    @Id
    private String id;
    private String username;
    private String password;
}
