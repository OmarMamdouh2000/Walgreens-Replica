package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;

@Builder
public class LoginAdminCommand extends Command {
    private String email;
    private String password;

    @Autowired
    AdminRepository adminRepository;


    @Override
    void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct

        adminRepository.loginAdmin(email, password);

        //add logic to add user session data to redis upon successful login
    }
}
