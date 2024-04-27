package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Role;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserCommand extends Command {
    private String email;
    private String password;
    private Role role;

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct
        try{
            List<String> response = userRepository.loginUser(email, password);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.get(0)), response.get(1)));
            this.role = Role.valueOf(response.get(2));
        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }

        //add logic to add user session data to redis upon successful login

    }
}
