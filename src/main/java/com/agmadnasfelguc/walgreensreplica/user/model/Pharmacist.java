package com.agmadnasfelguc.walgreensreplica.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "Pharmacist")
@Entity
@Getter
@Setter
public class Pharmacist extends User {
    @Id
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    public Pharmacist(String email, String password, String firstName, String lastName){
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
