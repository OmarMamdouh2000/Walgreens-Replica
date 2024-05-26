package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.PasswordHasher;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CheckOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UpdatePasswordResetCommand extends Command {
    @Setter
    private String email;
    @Setter
    private String password;
    @Setter
    private String otp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CheckOTPCommand checkOTPCommand;

    @Override
    public void execute() {
        try {
            setUpCheckOtpCommandAndExecute();
            if (checkOTPCommand.getState().getStatus().equals(ResponseState.Failure)) {
                this.setState(new ResponseStatus(ResponseState.Failure, checkOTPCommand.getState().getMessage()));
            }
            else{
                Tuple result = userRepository.updatePassword(email, PasswordHasher.hashPassword(password));
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this, e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

    private void setUpCheckOtpCommandAndExecute() {
        checkOTPCommand.setEmail(email);
        checkOTPCommand.setOtp(otp);
        checkOTPCommand.setOtpType(OTPTypes.RESETPASSWORD);
        checkOTPCommand.execute();
    }

}
