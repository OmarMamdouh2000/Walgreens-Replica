package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class CreateSessionCommand extends Command {

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private GetUserByIdCommand getUserByIdCommand;

    @Setter
    private String sessionId;
    @Getter
    private Map<String,Object> userMap;

    private String userId;

    @Override
    public void execute() {
        try{
            userId = JwtUtil.getUserIdFromToken(sessionId);
            setUpViewUserCommandAndExecute();
            if(getUserByIdCommand.getState().getStatus().equals(ResponseState.Failure)){
                this.setState(getUserByIdCommand.getState());
            }else{
                userMap = getUserByIdCommand.getUserInfo();
                sessionCache.createSession(sessionId, "user",userMap, 1, java.util.concurrent.TimeUnit.HOURS);
                this.setState(new ResponseStatus(ResponseState.Success, "Session Created"));
            }

        }catch(Exception e){
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateLogger(log, this.getState());

    }

    private void setUpViewUserCommandAndExecute() {
        getUserByIdCommand.setUserId(userId);
        getUserByIdCommand.execute();
    }
}
