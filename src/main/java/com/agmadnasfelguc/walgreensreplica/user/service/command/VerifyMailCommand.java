package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateUserSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class VerifyMailCommand extends Command {
    @Setter
    private String sessionId;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private Command generateOTPCommand;

    @Autowired
    private GetOrCreateUserSessionCommand userSessionCommand;

    private String userId;

    private String email;

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
                email = String.valueOf(sessionDetails.get("email"));
                setUpGenerateOtpCommandAndExecute();
                this.setState(generateOTPCommand.getState());
                if(this.getState().getStatus().equals(ResponseState.Success)) {
                    sessionCache.updateSessionDetails(sessionId, "user", Map.of("emailVerified", true));
                }
            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);

    }

    private void setUpGenerateOtpCommandAndExecute() {
        ((GenerateOTPCommand) generateOTPCommand).setEmail(email);
        ((GenerateOTPCommand) generateOTPCommand).setOtpType(OTPTypes.VERIFYMAIL);
        ((GenerateOTPCommand) generateOTPCommand).setSubject("Verify Email");
        generateOTPCommand.execute();
    }

    private void setUpGetOrCreateUserSessionCommandAndExecute(){
        userSessionCommand.setSessionId(sessionId);
        userSessionCommand.setUserId(userId);
        userSessionCommand.execute();
    }
}
