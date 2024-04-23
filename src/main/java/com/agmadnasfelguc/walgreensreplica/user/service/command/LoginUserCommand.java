package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct

        List<String> response = userRepository.loginUser(email, password);
        //print contents of response object
        System.out.println(response);

        //add logic to add user session data to redis upon successful login

    }
}
