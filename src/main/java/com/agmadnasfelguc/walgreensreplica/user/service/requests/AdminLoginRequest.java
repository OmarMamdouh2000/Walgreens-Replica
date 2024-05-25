package com.agmadnasfelguc.walgreensreplica.user.service.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminLoginRequest {
    private String username;
    private String password;
}
