package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class RegisterCommand extends Command {

    @Autowired
    private UserRepository userRepository;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    Logger logger = LoggerFactory.getLogger(RegisterCommand.class);

    @Override
    public void execute() {
        try{
            String dbState = userRepository.registerUser(firstName,lastName,email, password);
            this.setState(new ResponseStatus(ResponseState.Success, dbState));

        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }

        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }
}