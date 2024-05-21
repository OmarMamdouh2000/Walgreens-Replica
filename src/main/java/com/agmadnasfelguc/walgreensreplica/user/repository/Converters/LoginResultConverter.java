package com.agmadnasfelguc.walgreensreplica.user.repository.Converters;

import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class LoginResultConverter {

    public static LoginResult convertTupleToLoginResult(Tuple tuple) {
        return new LoginResult(
                (UUID) tuple.get("user_id"),
                tuple.get("status", String.class),
                tuple.get("message", String.class)
        );
    }
}
