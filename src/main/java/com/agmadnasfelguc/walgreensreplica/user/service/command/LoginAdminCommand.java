package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Builder
public class LoginAdminCommand extends Command {
    private String email;
    private String password;
    @Autowired
    AdminRepository adminRepository;


    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct
        try{
            List<String> response = adminRepository.loginAdmin(email, password);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.get(0)), response.get(1)));

        }catch(Exception e){
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }
        //add logic to add user session data to redis upon successful login
    }
}
