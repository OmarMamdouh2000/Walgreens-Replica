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

import java.util.Map;


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
    private boolean emailVerified;

    @Autowired
    private SessionCache sessionCache;

    Logger logger = LoggerFactory.getLogger(CreateSessionCommand.class);
    @Override
    public void execute() {
        try{
            this.sessionId = JwtUtil.generateToken(userId);
            Map<String,Object> userMap  = Map.of("userId",userId,"role",role,"email",email,"firstName",firstName,"lastName",lastName, "emailVerified",emailVerified);
            sessionCache.createSession(sessionId,"user",userMap);
            this.setState(new ResponseStatus(ResponseState.Success, "Session Created"));
            logger.info("Session Created");
        }catch(Exception e){
            logger.error(e.getMessage());
        }

    }
}
