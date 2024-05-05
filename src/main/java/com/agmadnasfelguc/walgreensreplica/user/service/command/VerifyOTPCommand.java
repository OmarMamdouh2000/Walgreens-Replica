package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;

import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class CheckOTPCommand extends Command{
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

    @Override
    public void execute() {
        String storedOtp = sessionCache.getOtp(email, com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes.TWOFACTORAUTH);
        if(storedOtp == null || !storedOtp.equals(otp)){
            this.setState(new ResponseStatus(ResponseState.FAILURE, "Invalid OTP"));
            return;
        }
        sessionCache.deleteOtp(email, com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes.TWOFACTORAUTH);

        //block 1
        ((CreateSessionCommand) createSessionCommand).setEmail(email);
        ((CreateSessionCommand) createSessionCommand).setUserId(userId);
        ((CreateSessionCommand) createSessionCommand).setRole(role);
        ((CreateSessionCommand) createSessionCommand).setFirstName(firstName);
        ((CreateSessionCommand) createSessionCommand).setLastName(lastName);
        createSessionCommand.execute();
        //block 1 will be replaced by a request to user management message queue for create session request

        this.setState(new ResponseStatus(ResponseState.SUCCESS, "OTP Verified"));

    }
}
