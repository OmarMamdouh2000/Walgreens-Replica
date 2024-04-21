package com.agmadnasfelguc.walgreensreplica.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Table(name = "Phone_Number")
@Entity
@Data
@Builder
public class PhoneNumber {
    @Id
    private String id;
    @Column
    private String extension;
    @Column
    private int number;
    @Column
    private boolean verified;
}
