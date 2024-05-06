package com.agmadnasfelguc.walgreensreplica.user.service.command.user;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.user.helpers.CheckEmailVerifiedCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.user.helpers.FindUserByIdCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class Update2FAStatusCommand extends Command {

    @Autowired
    private UserRepository userRepository;

    private String sessionId;
    private boolean twoFAEnabled;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private Command findUserByIdCommand;

    @Autowired
    private Command checkEmailVerifiedCommand;

    @Override
    public void execute() {
        try{
            String userID = sessionCache.getSessionDetails(sessionId).get("userId");
            if (userID == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                return;
            }

            Tuple result = userRepository.update2FAStatus(UUID.fromString(userID),twoFAEnabled);
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
    }


}