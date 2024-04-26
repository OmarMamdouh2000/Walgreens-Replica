package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.LoginUserCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.SendMailCommand;
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
    private final Command sendMailCommand;


    @Autowired
    public UserController(Command loginUserCommand, Command sendMailCommand) {
        this.loginUserCommand = loginUserCommand;
        this.sendMailCommand = sendMailCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
        ((LoginUserCommand)loginUserCommand).setEmail(email);
        ((LoginUserCommand)loginUserCommand).setPassword(password);
        loginUserCommand.execute();
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/sendMail")
    public ResponseEntity<Object> sendMail(@RequestParam String subject, @RequestParam String OTP) {
        ((SendMailCommand)sendMailCommand).setSubject(subject);
        ((SendMailCommand)sendMailCommand).setOTP(OTP);
        sendMailCommand.execute();
        return ResponseEntity.ok("Mail sent successfully");
    }
}
