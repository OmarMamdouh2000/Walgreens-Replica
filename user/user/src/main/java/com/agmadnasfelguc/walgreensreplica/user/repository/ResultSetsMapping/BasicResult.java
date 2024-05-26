package com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicResult {
    private String status;
    private String message;

}
