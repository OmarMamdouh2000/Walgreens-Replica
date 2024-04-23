package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.LoginUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/User")
public class UserController {

    private final Command loginUserCommand;

    @Autowired
    public UserController(Command loginUserCommand) {
        this.loginUserCommand = loginUserCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
        ((LoginUserCommand)loginUserCommand).setEmail(email);
        ((LoginUserCommand)loginUserCommand).setPassword(password);
        loginUserCommand.execute();
        return ResponseEntity.ok("Login successful");
    }
}
