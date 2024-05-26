package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.service.formulator.Formulator;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.KafkaPublisher;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.UserRequests;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public abstract class Command {

    private ResponseStatus state;
    private byte [] correlationId;
    private String replyTopic;

    @Autowired
    private UserRequests userRequests;

    public abstract void execute();

}
