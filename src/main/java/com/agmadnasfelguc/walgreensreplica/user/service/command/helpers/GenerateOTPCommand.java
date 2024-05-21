package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.OTPGenerator;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class GenerateOTPCommand extends Command {
    @Setter
    private String email;
    @Setter
    private OTPTypes otpType;
    @Setter
    private String subject;

    private String otp;

    @Autowired
    private SessionCache sessionCache;
    @Autowired
    private SendMailCommand sendMailCommand;

    @Override
    public void execute() {

        try{
            otp = OTPGenerator.generateOTP();
            sessionCache.storeOtp(email, otpType, otp, 10, java.util.concurrent.TimeUnit.MINUTES);

            setUpSendMailAndExecute();
            this.setState(sendMailCommand.getState());

        }catch (Exception e){
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateLogger(log, this.getState());

    }
    private void setUpSendMailAndExecute(){
        sendMailCommand.setSubject(subject);
        sendMailCommand.setEmail(email);
        sendMailCommand.setOTP(otp);
        sendMailCommand.execute();
    }
}
