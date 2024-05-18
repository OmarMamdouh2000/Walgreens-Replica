package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Data
@Service
public class CheckOTPCommand extends Command {
    private String email;
    private String otp;
    private OTPTypes otpType;
    @Autowired
    private SessionCache sessionCache;

    Logger logger = LoggerFactory.getLogger(CheckOTPCommand.class);

    @Override
    public void execute() {

        try{
            String storedOtp = sessionCache.getOtp(email, otpType);
            if (storedOtp == null || !storedOtp.equals(otp)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid OTP"));
                logger.error("Invalid OTP: " + otp);
                return;
            }
            sessionCache.deleteOtp(email, otpType);
            this.setState(new ResponseStatus(ResponseState.Success, "OTP Verified"));
            logger.info("OTP Verified");
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }
}
