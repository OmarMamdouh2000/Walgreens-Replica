package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;


@Slf4j
@Service
public class GetOrCreateUserSessionCommand extends Command {
    @Setter
    private String sessionId;

    @Setter
    private String userId;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private CreateSessionCommand createSessionCommand;

    @Getter
    private Map<String,Object> userMap;

    @Override
    public void execute() {
        try {
            if ((userMap = sessionCache.getSessionSection(sessionId,"user")) != null) {
                this.setState(new ResponseStatus(ResponseState.Success, "Session retrieved"));
            }
            else{
                setUpCreateUserSessionAndExecute();
                if(!createSessionCommand.getState().getStatus().equals(ResponseState.Failure)){
                    userMap = createSessionCommand.getUserMap();
                }
                this.setState(createSessionCommand.getState());
            }
        }
        catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }

        ResponseFormulator.formulateLogger(log, this.getState());
    }

    private void setUpCreateUserSessionAndExecute(){
        createSessionCommand.setSessionId(sessionId);
        createSessionCommand.execute();
    }
}
