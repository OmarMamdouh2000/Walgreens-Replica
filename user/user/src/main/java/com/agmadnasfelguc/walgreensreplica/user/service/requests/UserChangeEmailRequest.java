package com.agmadnasfelguc.walgreensreplica.user.service.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserChangeEmailRequest {
    private String sessionId;
    private String email;
    private String password;
}
