package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
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

    Logger logger = LoggerFactory.getLogger(Update2FAStatusCommand.class);

    @Override
    public void execute() {
        try{
            String userID = String.valueOf(sessionCache.getSessionSection(sessionId,"user").get("userId"));
            if (userID == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                logger.info(this.getState().getMessage());
                return;
            }

            Tuple result = userRepository.update2FAStatus(UUID.fromString(userID),twoFAEnabled);
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("2FA Status Updated" + response.getMessage());
            }else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Updating 2FA Status failed"+response.getStatus());
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            logger.error(this.getState().getMessage());
        }
    }


}