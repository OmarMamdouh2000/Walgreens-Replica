package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.repository.CustomerRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.LoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.CreateSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GenerateOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@EqualsAndHashCode(callSuper = true)
@Service
@Data
@NoArgsConstructor
public class LoginUserCommand extends Command {
    private String email;
    private String password;
    private String userId;
    private String sessionId;

    private UserRepository userRepository;
    private Command createSessionCommand;
    private Command generateOTPCommand;

    Logger logger = LoggerFactory.getLogger(LoginUserCommand.class);

    @Autowired
    public LoginUserCommand(UserRepository userRepository, Command createSessionCommand, Command generateOTPCommand) {
        this.userRepository = userRepository;
        this.createSessionCommand = createSessionCommand;
        this.generateOTPCommand = generateOTPCommand;
    }

    @Override
    public void execute() {
        System.out.println("LoginUserCommand.execute");
        System.out.println(email);
        System.out.println(password);
        System.out.println("testRun");
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct
        try{
            Tuple result = userRepository.loginUser(email, password);
            LoginResult response = LoginResultConverter.convertTupleToLoginResult(result);

            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Login Failed" + this.getState().getMessage());
                return;
            }
            else if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("You have logged in successfully" + response.getMessage());
            }
            this.userId = response.getUserId().toString();
            if(this.getState().getStatus().equals(ResponseState.Pending)){
                ((GenerateOTPCommand)generateOTPCommand).setEmail(email);
                ((GenerateOTPCommand)generateOTPCommand).setOtpType(OTPTypes.TWOFACTORAUTH);
                ((GenerateOTPCommand)generateOTPCommand).setFirstName(response.getFirst_name());
                ((GenerateOTPCommand)generateOTPCommand).setLastName(response.getLast_name());
                ((GenerateOTPCommand)generateOTPCommand).setSubject("Log in to your account");
                logger.info("You have logged in successfully" + response.getMessage());
                generateOTPCommand.execute();
                //block 1 will be replaced to a request to user management message queue for generate OTP request
                return;
            }
            //block 2
            ((CreateSessionCommand) createSessionCommand).setEmail(email);
            ((CreateSessionCommand) createSessionCommand).setUserId(userId);
            ((CreateSessionCommand) createSessionCommand).setRole(response.getRole());
            ((CreateSessionCommand) createSessionCommand).setFirstName(response.getFirst_name());
            ((CreateSessionCommand) createSessionCommand).setLastName(response.getLast_name());
            createSessionCommand.execute();
            //block 2 will be replaced to a request to user management message queue for create session request
        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            logger.error(e.getMessage());
        }


    }
}
