package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
@Data
public class UpdateEmailVerificationCommand extends Command {
    private String email;

    @Autowired
    private Command findUserByEmailCommand;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute() {

        ((FindUserByEmailCommand) findUserByEmailCommand).setEmail(email);
        findUserByEmailCommand.execute();
        if (findUserByEmailCommand.getState().getStatus().equals(ResponseState.Failure)) {
            this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
            return;
        }
        User user = ((FindUserByEmailCommand) findUserByEmailCommand).getUser();

        user.setEmailVerified(true);
        userRepository.save(user);

        this.setState(new ResponseStatus(ResponseState.Success, "Email verified"));
    }
}
