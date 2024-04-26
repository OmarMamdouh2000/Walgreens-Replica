package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.Date;
@Data
@Entity
@Table(name = "\"Customer\"")
public class Customer {
    @Id
    private String id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String address;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne
    @JoinColumn(name = "phone_id")
    private PhoneNumber phoneNumber;

    // getters and setters
}