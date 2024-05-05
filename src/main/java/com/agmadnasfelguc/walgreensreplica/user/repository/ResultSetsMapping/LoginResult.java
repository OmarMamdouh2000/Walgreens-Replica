package com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResult {
    private UUID userId;
    private String status;
    private String message;
    private String role;
    private String email;
    private String first_name;
    private String last_name;
}
