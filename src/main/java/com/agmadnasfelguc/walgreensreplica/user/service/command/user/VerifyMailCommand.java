package com.agmadnasfelguc.walgreensreplica.user.service.command.user;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.user.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@Service
public class VerifyMailCommand extends Command {
    private String sessionId;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private Command generateOTPCommand;

    @Override
    public void execute() {
        try {

            Map<String,String> details = sessionCache.getSessionDetails(sessionId);
            if(details == null){
                this.setState(new ResponseStatus(ResponseState.Failure, "Session not found"));
                return;
            }

            ((GenerateOTPCommand) generateOTPCommand).setEmail(details.get("email"));
            ((GenerateOTPCommand) generateOTPCommand).setOtpType(OTPTypes.VERIFYMAIL);
            ((GenerateOTPCommand) generateOTPCommand).setFirstName(details.get("firstName"));
            ((GenerateOTPCommand) generateOTPCommand).setLastName(details.get("lastName"));
            ((GenerateOTPCommand) generateOTPCommand).setSubject("Verify Email");
            generateOTPCommand.execute();
            this.setState(new ResponseStatus(ResponseState.Success, "Email sent"));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }

    }
}
