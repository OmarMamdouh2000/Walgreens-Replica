package com.agmadnasfelguc.walgreensreplica.user.service.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminAddPharmacist {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String sessionId;
}
