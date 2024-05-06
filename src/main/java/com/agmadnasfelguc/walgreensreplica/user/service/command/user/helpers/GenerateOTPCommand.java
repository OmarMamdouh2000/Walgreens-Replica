package com.agmadnasfelguc.walgreensreplica.user.service.command.user.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.OTPGenerator;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class GenerateOTPCommand extends Command {
    private String email;
    private String firstName;
    private String lastName;
    private OTPTypes otpType;
    private String subject;

    @Autowired
    private SessionCache sessionCache;
    @Autowired
    private Command checkEmailVerifiedCommand;
    @Autowired
    private Command sendMailCommand;


    @Override
    public void execute() {

        String otp = OTPGenerator.generateOTP();
        sessionCache.storeOtp(email, otpType, otp, 5, java.util.concurrent.TimeUnit.MINUTES);

        //block 1
        ((SendMailCommand) sendMailCommand).setSubject(subject);
        ((SendMailCommand) sendMailCommand).setEmail(email);
        ((SendMailCommand) sendMailCommand).setFirstName(firstName);
        ((SendMailCommand) sendMailCommand).setLastName(lastName);
        ((SendMailCommand) sendMailCommand).setOTP(otp);
        sendMailCommand.execute();
        //block 1 will be replaced to a request to user management message queue for send mail request

        if(sendMailCommand.getState().getStatus().equals(ResponseState.Failure)){
            this.setState(new ResponseStatus(ResponseState.Failure, "Failed to send mail"));
            return;
        }
        this.setState(new ResponseStatus(ResponseState.Success, "OTP sent"));

    }
}
