package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.AdminLoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.AdminLoginResult;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class LoginAdminCommand extends Command {
    private String username;
    private String password;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    Logger logger = LoggerFactory.getLogger(LoginAdminCommand.class);


    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct
          try{
            Tuple result = adminRepository.loginAdmin(username, password);
            AdminLoginResult response = AdminLoginResultConverter.convertTupleToLoginResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Login Failed" + this.getState().getMessage());
                return;
            }
            else if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("You have logged in successfully" + response.getMessage());
            }
            String sessionId = JwtUtil.generateToken(response.getUserId().toString());
            this.sessionCache.createAdminSession(sessionId, response.getUserId().toString(),username);

        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
              if(this.getState().getStatus().equals(ResponseState.Failure)){
                  logger.error(e.getMessage());
              }
        }
        //add logic to add user session data to redis upon successful login
    }
}
