package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
            this.setState(new ResponseStatus(ResponseState.SUCCESS, dbState));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }

    }
}