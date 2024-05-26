package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.ViewUserResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.ViewUserResult;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateUserSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetUserByIdCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Service
public class ViewUserCommand extends Command{

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private GetOrCreateUserSessionCommand userSessionCommand;

    @Setter
    private String sessionId;
    @Getter
    private ViewUserResult userInfo;

    private String userId;

    @Override
    public void execute() {
        try {
            userId = JwtUtil.getUserIdFromToken(sessionId);
            if(userId == null){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                setUpGetOrCreateUserSessionCommandAndExecute();
                Map<String, Object> sessionDetails = userSessionCommand.getUserMap();
                userInfo = ViewUserResultConverter.convertTupleToDTO(sessionDetails);
                this.setState(new ResponseStatus(ResponseState.Success, "User Found"));
            }

        } catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        if(this.getState().getStatus().equals(ResponseState.Success))
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("user",userInfo));
        else
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

    private void setUpGetOrCreateUserSessionCommandAndExecute(){
        userSessionCommand.setUserId(userId);
        userSessionCommand.setSessionId(sessionId);
        userSessionCommand.execute();

    }

}
