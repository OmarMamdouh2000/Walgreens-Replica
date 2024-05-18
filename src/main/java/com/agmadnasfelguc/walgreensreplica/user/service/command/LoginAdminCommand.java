package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.AdminLoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.AdminLoginResult;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
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

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class LoginAdminCommand extends Command {
    private String username;
    private String password;
    private String sessionId;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    Logger logger = LoggerFactory.getLogger(LoginAdminCommand.class);


    @Override
    public void execute() {
        try{
            Tuple result = adminRepository.loginAdmin(username, password);
            AdminLoginResult response = AdminLoginResultConverter.convertTupleToLoginResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Success)){
                this.sessionId = JwtUtil.generateToken(response.getUserId().toString());
                this.sessionCache.createAdminSession(sessionId, response.getUserId().toString(),username);
            }
        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        if(this.getState().getStatus().equals(ResponseState.Success)){
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("sessionId",sessionId));
        }else{
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
        }
    }
}
