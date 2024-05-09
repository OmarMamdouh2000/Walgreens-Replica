package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CheckOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
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

    @Override
    public void execute() {
        try {
            ((CheckOTPCommand) checkOTPCommand).setEmail(email);
            ((CheckOTPCommand) checkOTPCommand).setOtp(otp);
            ((CheckOTPCommand) checkOTPCommand).setOtpType(OTPTypes.RESETPASSWORD);
            checkOTPCommand.execute();

            if (checkOTPCommand.getState().getStatus().equals(ResponseState.Failure)) {
                this.setState(new ResponseStatus(ResponseState.Failure, checkOTPCommand.getState().getMessage()));
                return;
            }

            Tuple result = userRepository.updatePassword(email, password);
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);

            if(response.getStatus().equals(ResponseState.Failure.toString())){
                this.setState(new ResponseStatus(ResponseState.Failure, response.getMessage()));
                return;
            }
            this.setState(new ResponseStatus(ResponseState.Success, response.getMessage()));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
    }

}
