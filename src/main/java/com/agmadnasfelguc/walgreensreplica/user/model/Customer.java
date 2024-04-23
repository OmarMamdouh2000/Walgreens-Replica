package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
@Table(name = "Customer")
@Entity
@Getter
@Setter
public class Customer extends User {
    @Id
    private String id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String address;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "phone_id")
    private String phoneId;
    public Customer(String email, String password, String firstName, String lastName, String address, Date dateOfBirth){
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }
}
