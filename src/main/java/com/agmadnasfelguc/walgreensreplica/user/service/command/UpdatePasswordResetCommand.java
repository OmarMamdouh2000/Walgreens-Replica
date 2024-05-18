package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CheckOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class UpdatePasswordResetCommand extends Command {
    private String email;
    private String password;
    private String otp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Command checkOTPCommand;

    Logger logger = LoggerFactory.getLogger(UpdatePasswordResetCommand.class);

    @Override
    public void execute() {
        try {
            setUpCheckOtpCommandAndExecute();
            if (checkOTPCommand.getState().getStatus().equals(ResponseState.Failure)) {
                this.setState(new ResponseStatus(ResponseState.Failure, checkOTPCommand.getState().getMessage()));
            }
            else{
                Tuple result = userRepository.updatePassword(email, password);
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

    private void setUpCheckOtpCommandAndExecute() {
        ((CheckOTPCommand) checkOTPCommand).setEmail(email);
        ((CheckOTPCommand) checkOTPCommand).setOtp(otp);
        ((CheckOTPCommand) checkOTPCommand).setOtpType(OTPTypes.RESETPASSWORD);
        checkOTPCommand.execute();
    }

}
