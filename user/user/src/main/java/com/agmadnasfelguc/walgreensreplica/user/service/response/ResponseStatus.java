package com.agmadnasfelguc.walgreensreplica.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseStatus {
    private ResponseState status;
    private String message;

}
