package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;

import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
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
public class TwoFactorAuthLoginCommand extends Command {
    private String email;
    private String otp;
    private String userId;
    private String firstName;
    private String lastName;
    private String role;
    private boolean emailVerified;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private Command createSessionCommand;

    @Autowired
    private Command updateEmailVerificationCommand;

    Logger logger = LoggerFactory.getLogger(TwoFactorAuthLoginCommand.class);

    @Override
    public void execute() {
        try {
            String storedOtp = sessionCache.getOtp(email, OTPTypes.TWOFACTORAUTH);
            if (storedOtp == null || !storedOtp.equals(otp)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid OTP"));
            }
            else{
                sessionCache.deleteOtp(email, OTPTypes.TWOFACTORAUTH);
                setUpCreateSessionCommandAndExecute();
                this.setState(this.createSessionCommand.getState());
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        if(this.getState().getStatus().equals(ResponseState.Success)) {
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("sessionId", ((CreateSessionCommand) createSessionCommand).getSessionId()));
        }
        else{
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
        }
    }

    private void setUpCreateSessionCommandAndExecute() {
        ((CreateSessionCommand) createSessionCommand).setUserId(userId);
        ((CreateSessionCommand) createSessionCommand).setFirstName(firstName);
        ((CreateSessionCommand) createSessionCommand).setLastName(lastName);
        ((CreateSessionCommand) createSessionCommand).setRole(role);
        ((CreateSessionCommand) createSessionCommand).setEmail(email);
        ((CreateSessionCommand) createSessionCommand).setUserId(userId);
        ((CreateSessionCommand) createSessionCommand).setEmailVerified(emailVerified);
        createSessionCommand.execute();
    }
}
