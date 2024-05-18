package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class LogoutCommand extends Command {
    private String sessionId;

    @Autowired
    private SessionCache sessionCache;
    Logger logger = LoggerFactory.getLogger(LogoutCommand.class);
    @Override
    public void execute() {
        try{
            sessionCache.deleteCompleteSession(sessionId);
            this.setState(new ResponseStatus(ResponseState.Success, "Logged Out"));
        }catch (Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);

    }

}
