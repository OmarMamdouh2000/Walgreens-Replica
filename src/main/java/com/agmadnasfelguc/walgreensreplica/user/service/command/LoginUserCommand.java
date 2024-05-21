package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.LoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.PasswordHasher;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class LoginUserCommand extends Command {
    @Setter
    private String email;
    @Setter
    private String password;

    private UUID userId;
    private String sessionId;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreateSessionCommand createSessionCommand;
    @Autowired
    private GenerateOTPCommand generateOTPCommand;


    @Override
    public void execute() {
        try{
            Tuple result = userRepository.loginUser(email, PasswordHasher.hashPassword(password));
            LoginResult loginResult = LoginResultConverter.convertTupleToLoginResult(result);
            userId = loginResult.getUserId();
            this.setState(new ResponseStatus(ResponseState.valueOf(loginResult.getStatus()), loginResult.getMessage()));
            if(!this.getState().getStatus().equals(ResponseState.Failure)){
                if(this.getState().getStatus().equals(ResponseState.Pending)){
                    setUpGenerateOtpCommandAndExecute();
                }
                else{
                    setUpCreateSessionCommandAndExecute();
                    this.setState(createSessionCommand.getState());
                }
            }
            else{
                this.setState(new ResponseStatus(ResponseState.Failure, loginResult.getMessage()));
            }

        }catch(Exception e){
            ResponseFormulator.formulateException(this,e);
        }
        if(this.getState().getStatus().equals(ResponseState.Success)) {
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("sessionId", sessionId));
        } else {
            if(this.getState().getStatus().equals(ResponseState.Pending)){
                ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("userId", userId));
            }
            else {
                ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
            }
        }

    }

    private void setUpGenerateOtpCommandAndExecute(){
        generateOTPCommand.setEmail(email);
        generateOTPCommand.setOtpType(OTPTypes.TWOFACTORAUTH);
        generateOTPCommand.setSubject("Log in to your account");
        generateOTPCommand.execute();
    }

    private void setUpCreateSessionCommandAndExecute(){
        sessionId = JwtUtil.generateToken(String.valueOf(userId));
        createSessionCommand.setSessionId(sessionId);
        createSessionCommand.execute();
    }
}
