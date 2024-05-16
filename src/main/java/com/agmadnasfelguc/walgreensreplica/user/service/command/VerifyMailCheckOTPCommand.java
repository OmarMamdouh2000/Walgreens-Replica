package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CheckOTPCommand;
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
            Map<String, String> details = sessionCache.getSessionDetails(sessionId);

            ((CheckOTPCommand) checkOTPCommand).setEmail(details.get("email"));
            ((CheckOTPCommand) checkOTPCommand).setOtp(otp);
            ((CheckOTPCommand) checkOTPCommand).setOtpType(OTPTypes.VERIFYMAIL);
            checkOTPCommand.execute();

            if (checkOTPCommand.getState().getStatus() == ResponseState.Failure) {
                this.setState(new ResponseStatus(ResponseState.Failure, checkOTPCommand.getState().getMessage()));
                logger.error(checkOTPCommand.getState().getMessage());
                return;
            }

            Tuple result =  userRepository.verifyEmail(UUID.fromString(details.get("userId")));
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
//            System.out.println(response);

            if(response.getStatus().equals(ResponseState.Failure.toString())){
                this.setState(new ResponseStatus(ResponseState.Failure, response.getMessage()));
                logger.error("Sending Verify Email Failed");
                return;
            }
            this.setState(new ResponseStatus(ResponseState.Success, response.getMessage()));
            logger.info("Sending Verify Email Success" + response.getMessage());
//            System.out.println(this.getState());
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            logger.error(e.getMessage());
        }

    }
}
