package com.agmadnasfelguc.walgreensreplica.user.repository.Converters;

import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.AdminLoginResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import jakarta.persistence.Tuple;

import java.util.UUID;

public class BasicResultConverter {
    public static BasicResult convertTupleToBasicResult(Tuple tuple) {
        return new BasicResult(
                tuple.get("status", String.class),
                tuple.get("message", String.class)
        );
    }
}
