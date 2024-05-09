package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Data
@Service
public class FindUserByIdCommand extends Command {
    private UUID userId;

    private User user;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void execute() {
        try{
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                this.user = userOptional.get();
            } else {
                this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
            }

        }catch (Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
    }
}
