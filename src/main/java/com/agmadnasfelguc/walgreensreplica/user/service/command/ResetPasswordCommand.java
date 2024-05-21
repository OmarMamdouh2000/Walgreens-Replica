package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.FindUserByEmailCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResetPasswordCommand extends Command {
    @Setter
    private String email;

    @Autowired
    private FindUserByEmailCommand findUserByEmailCommand;

    @Autowired
    private Command generateOTPCommand;


    @Override
    public void execute() {
        try{
            findUserByEmailCommand.setEmail(email);
            findUserByEmailCommand.execute();
            if (findUserByEmailCommand.getState().getStatus().equals(ResponseState.Failure)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
            }else{
                if(!findUserByEmailCommand.getUser().isEmailVerified()){
                    this.setState(new ResponseStatus(ResponseState.Failure, "Email is not verified"));
                }
                else{
                    setUpGenerateOtpCommandAndExecute();
                    this.setState(generateOTPCommand.getState());
                }
            }

        }catch (Exception e){
            ResponseFormulator.formulateException(this, e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);

    }

    private void setUpGenerateOtpCommandAndExecute(){
        ((GenerateOTPCommand) generateOTPCommand).setEmail(email);
        ((GenerateOTPCommand) generateOTPCommand).setOtpType(OTPTypes.RESETPASSWORD);
        ((GenerateOTPCommand) generateOTPCommand).setSubject("Reset Password");
        generateOTPCommand.execute();
    }
}
