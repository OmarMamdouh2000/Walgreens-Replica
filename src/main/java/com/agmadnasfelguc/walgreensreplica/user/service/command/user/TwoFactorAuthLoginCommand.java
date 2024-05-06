package com.agmadnasfelguc.walgreensreplica.user.service.command.user;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.user.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
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



    @Override
    public void execute() {
        try {
            String storedOtp = sessionCache.getOtp(email, OTPTypes.TWOFACTORAUTH);
            if (storedOtp == null || !storedOtp.equals(otp)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid OTP"));
                return;
            }
            sessionCache.deleteOtp(email, OTPTypes.TWOFACTORAUTH);

            //block 1
            ((CreateSessionCommand) createSessionCommand).setEmail(email);
            ((CreateSessionCommand) createSessionCommand).setUserId(userId);
            ((CreateSessionCommand) createSessionCommand).setRole(role);
            ((CreateSessionCommand) createSessionCommand).setFirstName(firstName);
            ((CreateSessionCommand) createSessionCommand).setLastName(lastName);
            createSessionCommand.execute();
            //block 1 will be replaced by a request to user management message queue for create session request
            this.setState(new ResponseStatus(ResponseState.Success, "Session Created"));

        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }

    }
}
