package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.service.formulator.Formulator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Command {
    private Formulator formulator;
    public abstract void execute();
}
