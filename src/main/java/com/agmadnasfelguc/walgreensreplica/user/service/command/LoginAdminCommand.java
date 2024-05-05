package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.AdminLoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.LoginResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.AdminLoginResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.LoginResult;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class LoginAdminCommand extends Command {
    private String username;
    private String password;
    @Autowired
    AdminRepository adminRepository;


    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct
        try{
            Tuple result = adminRepository.loginAdmin(username, password);
            AdminLoginResult response = AdminLoginResultConverter.convertTupleToLoginResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus().toUpperCase()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.FAILURE)){
                return;
            }

        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }
        //add logic to add user session data to redis upon successful login
    }
}
