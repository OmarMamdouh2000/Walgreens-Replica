package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.model.Customer;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CheckEmailVerifiedCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.FindCustomerByEmailCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class ResetPasswordCommand extends Command {
    private String email;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Command findCustomerByEmailCommand;

    @Autowired
    private Command checkEmailVerifiedCommand;

    @Autowired
    private Command generateOTPCommand;

    Logger logger = LoggerFactory.getLogger(ResetPasswordCommand.class);


    @Override
    public void execute() {
        try{
            ((FindCustomerByEmailCommand) findCustomerByEmailCommand).setEmail(email);
            findCustomerByEmailCommand.execute();
            if (findCustomerByEmailCommand.getState().getStatus().equals(ResponseState.Failure)) {
                this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
                logger.error(this.getState().getMessage());
                return;
            }
            Customer customer = ((FindCustomerByEmailCommand) findCustomerByEmailCommand).getCustomer();

            ((CheckEmailVerifiedCommand) checkEmailVerifiedCommand).setUser(customer.getUser());
            checkEmailVerifiedCommand.execute();
            if(checkEmailVerifiedCommand.getState().getStatus().equals(ResponseState.Failure)){
                this.setState(new ResponseStatus(ResponseState.Failure, "Email is not verified"));
                logger.error(this.getState().getMessage());
                return;
            }

            ((GenerateOTPCommand) generateOTPCommand).setEmail(email);
            ((GenerateOTPCommand) generateOTPCommand).setOtpType(OTPTypes.RESETPASSWORD);
            ((GenerateOTPCommand) generateOTPCommand).setFirstName(customer.getFirstName());
            ((GenerateOTPCommand) generateOTPCommand).setLastName(customer.getLastName());
            ((GenerateOTPCommand) generateOTPCommand).setSubject("Reset Password");
            generateOTPCommand.execute();
            if(generateOTPCommand.getState().getStatus().equals(ResponseState.Failure)){
                this.setState(new ResponseStatus(ResponseState.Failure, "Failed to generate OTP"));
                logger.error(this.getState().getMessage());
                return;
            }
            this.setState(new ResponseStatus(ResponseState.Success, generateOTPCommand.getState().getMessage()));

            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("Password Reset successfully"+this.getState().getMessage());
            }
            else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Resetting password failed");
            }

        }catch (Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error(e.getMessage());
            }
        }

    }
}
