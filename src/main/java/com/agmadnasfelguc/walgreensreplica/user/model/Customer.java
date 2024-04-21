package com.agmadnasfelguc.walgreensreplica.user.model;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Table(name = "Customer")
@Entity
@Data
@Getter
@Setter
@Builder
public class Customer extends User {
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
}
