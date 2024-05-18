package com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViewUserResult {
    private String userId;
    private String email;
    private String role;
    private String status;
    private boolean emailVerified;
    private boolean twoFactorAuthEnabled;
    private String firstName;
    private String lastName;
    private String address;
    private String dateOfBirth;
    private String imageUrl;
    private String gender;
    private String phoneNumber;
    private String extension;
}

