package com.agmadnasfelguc.walgreensreplica.user.service.formulator;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public abstract class Formulator {
    private String receiver;
    public abstract JSONObject formulate(Object o);
}
