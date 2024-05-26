package com.agmadnasfelguc.walgreensreplica.user.service.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminAddAdmin {
    private String username;
    private String password;
    private String sessionId;
}
