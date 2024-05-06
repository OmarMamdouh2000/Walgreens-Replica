package com.agmadnasfelguc.walgreensreplica.user.service.command.user;

import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public void execute() {
        try{
            String dbState = userRepository.registerUser(firstName,lastName,email, password);
            this.setState(new ResponseStatus(ResponseState.Success, dbState));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }

    }
}