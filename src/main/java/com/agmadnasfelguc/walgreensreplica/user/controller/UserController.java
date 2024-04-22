package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.RegisterCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.ChangeEmailCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.ChangePasswordCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.EditDetailsCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final Command registerCommand;
    private final Command changeEmailCommand;
    private final Command changePasswordCommand;
    private final Command editDetailsCommand;

    @Autowired
    public UserController(Command registerCommand,Command changeEmailCommand , Command changePasswordCommand , Command editDetailsCommand) {
        this.registerCommand = registerCommand;
        this.changeEmailCommand = changeEmailCommand;
        this.changePasswordCommand = changePasswordCommand;
        this.editDetailsCommand = editDetailsCommand;
    }

    @PostMapping("/register_user")
    public void registerUser(@RequestBody UserRegistrationRequest request) {
        // Assuming the request contains first name, last name, email, and password

        // Set the user registration details in the command class
        registerCommand.setFirstName(request.getFirstName());
        registerCommand.setLastName(request.getLastName());
        registerCommand.setEmail(request.getEmail());
        registerCommand.setPassword(request.getPassword());

        registerCommand.execute();
    }


    @PostMapping("/ChangeEmail")
    public void changeEmail(@RequestBody UserRegistrationRequest request) {
        // Assuming the request contains User Id ,and email.

        // Set the user registration details in the command class
        changeEmailCommand.setUserID(request.getUserID());
        changeEmailCommand.setEmail(request.getEmail());

        changeEmailCommand.execute();
    }

    @PostMapping("/ChangePassword")
    public void changeEmail(@RequestBody UserRegistrationRequest request) {
        // Assuming the request contains User Id ,and password.

        // Set the user registration details in the command class
        changePasswordCommand.setUserID(request.getUserID());
        changePasswordCommand.setEmail(request.getPassword());

        changePasswordCommand.execute();
    }


    @PostMapping("/edit_personal_details")
    public void registerUser(@RequestBody UserRegistrationRequest request) {
        // Assuming the request contains user Id, address, date of birth, gender, phone number, and password

        // Set the user registration details in the command class
        editDetailsCommand.setUserID(request.getUserID());
        editDetailsCommand.setDateofbirth(request.getDateofbirth());
        editDetailsCommand.setGender(request.getGender());
        editDetailsCommand.setPhoneNumber(request.getPhoneNumber());
        editDetailsCommand.setExtension(request.getExtension());

        editDetailsCommand.execute();
    }
}
