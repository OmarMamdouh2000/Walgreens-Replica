package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class CreateSessionCommand extends Command {
    private String email;
    private String userId;
    private String role;
    private String sessionId;
    private String firstName;
    private String lastName;

    @Autowired
    private SessionCache sessionCache;

    Logger logger = LoggerFactory.getLogger(CreateSessionCommand.class);
    @Override
    public void execute() {
        try{
            this.sessionId = JwtUtil.generateToken(userId);
            sessionCache.createSession(sessionId,userId,role,email,firstName,lastName);
            this.setState(new ResponseStatus(ResponseState.Success, "Session Created"));
            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("Session Created");
            }else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Failed to create session");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }


    }
}
