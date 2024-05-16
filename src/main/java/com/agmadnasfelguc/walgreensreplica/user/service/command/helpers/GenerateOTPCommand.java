package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.OTPGenerator;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
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

    Logger logger = LoggerFactory.getLogger(GenerateOTPCommand.class);


    @Override
    public void execute() {

        try{
            String otp = OTPGenerator.generateOTP();
            sessionCache.storeOtp(email, otpType, otp, 5, java.util.concurrent.TimeUnit.MINUTES);

            logger.info("OTP generated");
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
                logger.error("Failed to send mail");
                return;
            }
            this.setState(new ResponseStatus(ResponseState.Success, "OTP sent"));

            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("OTP sent successfully");
            }else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("OTP sent failed");
            }
            logger.info(this.getState().getMessage());
        }catch (Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure,e.getMessage()));
            logger.error(e.getMessage());
        }



    }
}
