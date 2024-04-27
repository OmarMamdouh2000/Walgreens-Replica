package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
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
public class ChangeEmailCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    private String userID;
    private String password;
    private String email;

    @Override
    public void execute() {
        try{
            List<String> response = userRepository.changeEmail(userID,password,email);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.get(0)), response.get(1) ));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }


    }
}