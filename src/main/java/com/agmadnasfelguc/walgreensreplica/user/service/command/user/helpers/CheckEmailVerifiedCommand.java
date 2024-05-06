package com.agmadnasfelguc.walgreensreplica.user.service.command.user.helpers;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Data
@Service
public class CheckEmailVerifiedCommand extends Command {
    private String email;

    private User user;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute() {
        try {
            if(user.isEmailVerified()) {
                this.setState(new ResponseStatus(ResponseState.Success, "Email is verified"));
            } else {
                this.setState(new ResponseStatus(ResponseState.Failure, "Email is not verified"));
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
    }


}
