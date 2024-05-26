package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CheckOTPCommand extends Command {
    @Setter
    private String email;
    @Setter
    private String otp;
    @Setter
    private OTPTypes otpType;
    @Autowired
    private SessionCache sessionCache;

    @Override
    public void execute() {

        try{
            String storedOtp = sessionCache.getOtp(email, otpType);
            System.out.println("otp check");
            System.out.println(storedOtp);
            System.out.println(otp);
            if (storedOtp == null || !storedOtp.equals(otp)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid OTP"));
            }
            else{
                sessionCache.deleteOtp(email, otpType);
                this.setState(new ResponseStatus(ResponseState.Success, "OTP Verified"));
            }
        }catch(Exception e){
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateLogger(log,this.getState());

    }
}
