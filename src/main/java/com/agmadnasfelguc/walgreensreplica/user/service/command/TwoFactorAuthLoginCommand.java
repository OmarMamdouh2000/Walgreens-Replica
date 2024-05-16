package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;

import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                logger.error(this.getState().getMessage());
                return;
            }
            sessionCache.deleteOtp(email, OTPTypes.TWOFACTORAUTH);
            logger.info("Successfully deleted OTP");

            //block 1
            ((CreateSessionCommand) createSessionCommand).setEmail(email);
            ((CreateSessionCommand) createSessionCommand).setUserId(userId);
            ((CreateSessionCommand) createSessionCommand).setRole(role);
            ((CreateSessionCommand) createSessionCommand).setFirstName(firstName);
            ((CreateSessionCommand) createSessionCommand).setLastName(lastName);
            createSessionCommand.execute();
            //block 1 will be replaced by a request to user management message queue for create session request
            this.setState(new ResponseStatus(ResponseState.Success, "Session Created"));

            if(this.getState().getStatus().equals(ResponseState.Success)){

            }
            else if(this.getState().getStatus().equals(ResponseState.Failure)){

            }

        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }

    }
}
