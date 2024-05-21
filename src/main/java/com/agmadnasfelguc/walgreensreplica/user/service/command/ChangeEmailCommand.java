package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;

import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.PasswordHasher;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateUserSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;

import lombok.EqualsAndHashCode;
import lombok.Setter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class ChangeEmailCommand extends Command {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionCache sessionCache;
    @Autowired
    private GetOrCreateUserSessionCommand userSessionCommand;

    @Setter
    private String sessionId;
    @Setter
    private String password;
    @Setter
    private String email;

    private String userId;

    @Override
    public void execute() {
        try{
            userId = JwtUtil.getUserIdFromToken(sessionId);
            if(userId == null){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                Tuple result = userRepository.changeEmail(UUID.fromString(userId), PasswordHasher.hashPassword(password),email);
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
                if(this.getState().getStatus().equals(ResponseState.Success)){
                    Map<String,Object> sessionDetails = sessionCache.getSessionSection(sessionId,"user");
                    if(sessionDetails != null){
                        sessionCache.updateSessionDetails(sessionId,"user", Map.of("email",email, "emailVerified",false));
                    }
                    else{
                        setUpUserSessionCommandAndExecute();
                    }
                }
            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);

    }

    private void setUpUserSessionCommandAndExecute() {
        userSessionCommand.setSessionId(sessionId);
        userSessionCommand.setUserId(userId);
        userSessionCommand.execute();
    }
}