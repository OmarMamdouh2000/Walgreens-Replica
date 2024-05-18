package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class ChangePasswordCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionCache sessionCache;

    private String sessionID;
    private String oldPassword;
    private String newPassword;

    Logger logger = LoggerFactory.getLogger(ChangePasswordCommand.class);


    @Override
    public void execute() {
        try{
            String userID = String.valueOf(sessionCache.getSessionSection(sessionID,"user").get("userId"));
            if(userID.equals("null")){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                Tuple result = userRepository.changePassword(UUID.fromString(userID),oldPassword,newPassword);
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }
}