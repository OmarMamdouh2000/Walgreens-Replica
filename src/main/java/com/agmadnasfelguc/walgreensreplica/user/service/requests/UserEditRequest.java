package com.agmadnasfelguc.walgreensreplica.user.service.requests;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserEditRequest {
    private String userID;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phoneNumber;
    private String address;
    private String extension;
    private Gender gender;

}
