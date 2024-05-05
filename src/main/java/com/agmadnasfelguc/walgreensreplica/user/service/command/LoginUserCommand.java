package com.agmadnasfelguc.walgreensreplica.user.service.command;
import com.agmadnasfelguc.walgreensreplica.user.cache.OTPTypes;
import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.CustomerRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.LoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
public class LoginUserCommand extends Command {
    private String email;
    private String password;
    private String userId;
    private String sessionId;

    @Autowired
    UserRepository userRepository;

    //this will be removed later
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Command createSessionCommand;

    @Autowired
    private Command generateOTPCommand;

    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct
//        try{
            Tuple result = userRepository.loginUser(email, password);
            LoginResult response = LoginResultConverter.convertTupleToLoginResult(result);
            System.out.println(response);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus().toUpperCase()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.FAILURE)){
                return;
            }
            this.userId = response.getUserId().toString();
            if(this.getState().getStatus().equals(ResponseState.PENDING)){
                //block 1
                ((GenerateOTPCommand)generateOTPCommand).setEmail(email);
                ((GenerateOTPCommand)generateOTPCommand).setOtpType(OTPTypes.TWOFACTORAUTH);
                ((GenerateOTPCommand)generateOTPCommand).setFirstName(response.getFirst_name());
                ((GenerateOTPCommand)generateOTPCommand).setLastName(response.getLast_name());
                ((GenerateOTPCommand)generateOTPCommand).setSubject("Log in to your account");
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
//        }catch(Exception e){
//            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
//        }


    }
}
