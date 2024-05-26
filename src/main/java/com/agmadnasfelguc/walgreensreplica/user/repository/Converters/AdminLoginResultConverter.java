package com.agmadnasfelguc.walgreensreplica.user.repository.Converters;

import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.AdminLoginResult;
import jakarta.persistence.Tuple;

import java.util.UUID;

public class AdminLoginResultConverter {
    public static AdminLoginResult convertTupleToLoginResult(Tuple tuple) {
        return new AdminLoginResult(
                (UUID) tuple.get("admin_id"),
                tuple.get("status", String.class),
                tuple.get("message", String.class)
        );
    }
}
