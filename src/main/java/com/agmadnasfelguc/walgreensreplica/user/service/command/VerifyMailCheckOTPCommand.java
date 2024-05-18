package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CheckOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Data
public class VerifyMailCheckOTPCommand extends Command {
    private String sessionId;
    private String otp;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Command checkOTPCommand;

    Logger logger = LoggerFactory.getLogger(VerifyMailCheckOTPCommand.class);

    @Override
    public void execute() {
        try {
            Map<String, Object> details = sessionCache.getSessionSection(sessionId, "user");
            setUpCheckOtpCommandAndExecute(details);
            if (checkOTPCommand.getState().getStatus().equals(ResponseState.Failure)) {
                this.setState(new ResponseStatus(ResponseState.Failure, checkOTPCommand.getState().getMessage()));
            }

            else{
                Tuple result =  userRepository.verifyEmail(UUID.fromString(String.valueOf(details.get("userId"))));
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));

            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);

    }

    private void setUpCheckOtpCommandAndExecute(Map<String, Object> details) {
        System.out.println("otp check");
        System.out.println(otp);
        ((CheckOTPCommand) checkOTPCommand).setEmail(String.valueOf(details.get("email")));
        ((CheckOTPCommand) checkOTPCommand).setOtp(otp);
        ((CheckOTPCommand) checkOTPCommand).setOtpType(OTPTypes.VERIFYMAIL);
        checkOTPCommand.execute();
    }
}
