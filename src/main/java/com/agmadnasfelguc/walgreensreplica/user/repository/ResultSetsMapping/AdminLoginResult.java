package com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AdminLoginResult {
    private UUID userId;
    private String status;
    private String message;
}
