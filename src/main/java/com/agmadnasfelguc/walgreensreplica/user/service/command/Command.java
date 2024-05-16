package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.service.formulator.Formulator;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.KafkaPublisher;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Command {

    private KafkaPublisher publisher;
    private ResponseStatus state;

    public abstract void execute();
}
