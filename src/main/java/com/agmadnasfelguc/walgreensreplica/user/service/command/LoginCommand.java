package com.agmadnasfelguc.walgreensreplica.user.service.command;
import lombok.Builder;

@Builder
public class LoginCommand extends Command {
    private String email;
    private String password;

    @Override
    public void execute() {
        //add logic to check if user is logged in, i.e. data already available in redis

        //call stored procedure from postgres to check if user exists and password is correct



        //add logic to add user session data to redis upon successful login

    }
}
