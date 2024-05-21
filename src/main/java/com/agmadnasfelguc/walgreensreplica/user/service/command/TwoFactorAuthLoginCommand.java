package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;

import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class TwoFactorAuthLoginCommand extends Command {
    @Setter
    private String userId;
    @Setter
    private String otp;
    @Setter
    private String email;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private CreateSessionCommand createSessionCommand;

    private String sessionId;

    @Override
    public void execute() {
        try {
            String storedOtp = sessionCache.getOtp(email, OTPTypes.TWOFACTORAUTH);
            sessionCache.deleteOtp(email, OTPTypes.TWOFACTORAUTH);
            if (storedOtp == null || !storedOtp.equals(otp)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid OTP"));
            }
            else{
                sessionId = JwtUtil.generateToken(userId);
                setUpCreateSessionCommandAndExecute();
                if(createSessionCommand.getState().getStatus().equals(ResponseState.Failure)) {
                    this.setState(createSessionCommand.getState());
                }
                else {
                    this.setState(new ResponseStatus(ResponseState.Success, "OTP Verified and Session Created"));
                }
            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this, e);
        }
        if(this.getState().getStatus().equals(ResponseState.Success)) {
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("sessionId", sessionId ));
        }
        else{
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
        }
    }

    private void setUpCreateSessionCommandAndExecute() {
        createSessionCommand.setSessionId(sessionId);
        createSessionCommand.execute();
    }
}
