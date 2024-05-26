package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.PasswordHasher;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class RegisterCommand extends Command {

    @Autowired
    private UserRepository userRepository;

    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String email;
    @Setter
    private String password;



    @Override
    public void execute() {
        try{
            System.out.println("Password here");
            System.out.println(password);
            System.out.println(PasswordHasher.hashPassword(password));
            String dbState = userRepository.registerUser(firstName,lastName,email, PasswordHasher.hashPassword(password));
            this.setState(new ResponseStatus(ResponseState.Success, dbState));

        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }

        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }
}