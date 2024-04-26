package com.agmadnasfelguc.walgreensreplica.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Table(name = "\"Phone_Number\"")
public class PhoneNumber {
    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private int number;

    @Column(nullable = false)
    private String extension;
}
