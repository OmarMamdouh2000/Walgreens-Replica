package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.LoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
@NoArgsConstructor
public class LoginUserCommand extends Command {
    private String email;
    private String password;
    private String userId;
    private String sessionId;

    private UserRepository userRepository;
    private Command createSessionCommand;
    private Command generateOTPCommand;

    Logger logger = LoggerFactory.getLogger(LoginUserCommand.class);

    @Autowired
    public LoginUserCommand(UserRepository userRepository, Command createSessionCommand, Command generateOTPCommand) {
        this.userRepository = userRepository;
        this.createSessionCommand = createSessionCommand;
        this.generateOTPCommand = generateOTPCommand;
    }

    @Override
    public void execute() {
        try{
            Tuple result = userRepository.loginUser(email, password);
            LoginResult response = LoginResultConverter.convertTupleToLoginResult(result);

            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(!this.getState().getStatus().equals(ResponseState.Failure)){
                this.userId = response.getUserId().toString();
                if(this.getState().getStatus().equals(ResponseState.Pending)){
                    setUpGenerateOtpCommandAndExecute(response);
                }
                else{
                    setUpCreateSessionCommandAndExecute(response);
                    this.sessionId = ((CreateSessionCommand) createSessionCommand).getSessionId();
                }
            }

        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            e.printStackTrace();
        }
        if(this.getState().getStatus().equals(ResponseState.Success)) {
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("sessionId", sessionId));
        } else {
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
        }

    }

    private void setUpGenerateOtpCommandAndExecute(LoginResult response){
        ((GenerateOTPCommand)generateOTPCommand).setEmail(email);
        ((GenerateOTPCommand)generateOTPCommand).setOtpType(OTPTypes.TWOFACTORAUTH);
        ((GenerateOTPCommand)generateOTPCommand).setFirstName(response.getFirst_name());
        ((GenerateOTPCommand)generateOTPCommand).setLastName(response.getLast_name());
        ((GenerateOTPCommand)generateOTPCommand).setSubject("Log in to your account");
        generateOTPCommand.execute();
    }

    private void setUpCreateSessionCommandAndExecute(LoginResult response){
        ((CreateSessionCommand) createSessionCommand).setEmail(email);
        ((CreateSessionCommand) createSessionCommand).setUserId(userId);
        ((CreateSessionCommand) createSessionCommand).setRole(response.getRole());
        ((CreateSessionCommand) createSessionCommand).setFirstName(response.getFirst_name());
        ((CreateSessionCommand) createSessionCommand).setLastName(response.getLast_name());
        ((CreateSessionCommand) createSessionCommand).setEmailVerified(response.isEmail_verified());
        createSessionCommand.execute();
    }
}
