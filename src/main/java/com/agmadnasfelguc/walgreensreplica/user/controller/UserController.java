package com.agmadnasfelguc.walgreensreplica.user.controller;
import com.agmadnasfelguc.walgreensreplica.user.service.command.*;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserChangeEmailRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserChangePasswordRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserEditRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/User")
public class UserController {
    private final Command registerCommand;
    private final Command changeEmailCommand;
    private final Command changePasswordCommand;
    private final Command editDetailsCommand;

    private final Command loginUserCommand;
    private final Command sendMailCommand;
    private final Command logoutCommand;
    private final Command verifyOTPCommand;

    @Autowired
    public UserController(Command registerCommand, Command changeEmailCommand, Command changePasswordCommand, Command editDetailsCommand, Command loginUserCommand, Command sendMailCommand, Command logoutCommand, Command verifyOTPCommand) {
        this.registerCommand = registerCommand;
        this.changeEmailCommand = changeEmailCommand;
        this.changePasswordCommand = changePasswordCommand;
        this.editDetailsCommand = editDetailsCommand;
        this.loginUserCommand = loginUserCommand;
        this.sendMailCommand = sendMailCommand;
        this.logoutCommand = logoutCommand;
        this.verifyOTPCommand = verifyOTPCommand;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistrationRequest request) {
        // Assuming the request contains first name, last name, email, and password

        // Set the user registration details in the command class
        ((RegisterCommand)registerCommand).setFirstName(request.getFirstName());
        ((RegisterCommand)registerCommand).setLastName(request.getLastName());
        ((RegisterCommand)registerCommand).setEmail(request.getEmail());
        ((RegisterCommand)registerCommand).setPassword(request.getPassword());

        registerCommand.execute();
        return formulateResponse(registerCommand);
    }


    @PostMapping("/changeEmail")
    public ResponseEntity<Object> changeEmail(@RequestBody UserChangeEmailRequest request) {
        // Assuming the request contains User Id ,and email.

        // Set the user registration details in the command class
        ((ChangeEmailCommand)changeEmailCommand).setSessionID(request.getSessionId());
        ((ChangeEmailCommand)changeEmailCommand).setEmail(request.getEmail());
        ((ChangeEmailCommand)changeEmailCommand).setPassword(request.getPassword());

        changeEmailCommand.execute();
        return formulateResponse(changeEmailCommand);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changeEmail(@RequestBody UserChangePasswordRequest request) {
        // Assuming the request contains UserId ,and password.

        // Set the user registration details in the command class
        ((ChangePasswordCommand)changePasswordCommand).setSessionID(request.getSessionId());
        ((ChangePasswordCommand)changePasswordCommand).setOldPassword(request.getOldPassword());
        ((ChangePasswordCommand)changePasswordCommand).setNewPassword(request.getNewPassword());

        changePasswordCommand.execute();
        return formulateResponse(changePasswordCommand);
    }


    @PostMapping("/editPersonalDetails")
    public ResponseEntity<Object> editPersonalDetails(@RequestBody UserEditRequest request) {
        // Assuming the request contains userId, address, date of birth, gender, phone number, and password

        //check that the request contains all the required fields

        // Set the user registration details in the command class
        ((EditDetailsCommand)editDetailsCommand).setSessionID(request.getSessionId());
        ((EditDetailsCommand)editDetailsCommand).setDateOfBirth(request.getDateOfBirth());
        ((EditDetailsCommand)editDetailsCommand).setGender(request.getGender());
        ((EditDetailsCommand)editDetailsCommand).setPhoneNumber(request.getPhoneNumber());
        ((EditDetailsCommand)editDetailsCommand).setExtension(request.getExtension());
        ((EditDetailsCommand)editDetailsCommand).setAddress(request.getAddress());

        editDetailsCommand.execute();
        return formulateResponse(editDetailsCommand);


    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
        ((LoginUserCommand)loginUserCommand).setEmail(email);
        ((LoginUserCommand)loginUserCommand).setPassword(password);
        loginUserCommand.execute();
        if(loginUserCommand.getState().toString().equals("FAILURE"))
            return ResponseEntity.badRequest().body(loginUserCommand.getState().getMessage());
        return ResponseEntity.ok(loginUserCommand.getState().getMessage());

    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam String sessionId) {
        // Assuming the request contains sessionId

        // Set the user registration details in the command class
        ((LogoutCommand)logoutCommand).setSessionId(sessionId);

        logoutCommand.execute();
        return formulateResponse(logoutCommand);
    }

    @PostMapping("/verifyOTP")
    public ResponseEntity<Object> verifyOTP(@RequestParam String email, @RequestParam String OTP, @RequestParam String userId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String role) {
        ((VerifyOTPCommand)verifyOTPCommand).setEmail(email);
        ((VerifyOTPCommand)verifyOTPCommand).setOtp(OTP);
        ((VerifyOTPCommand)verifyOTPCommand).setUserId(userId);
        ((VerifyOTPCommand)verifyOTPCommand).setFirstName(firstName);
        ((VerifyOTPCommand)verifyOTPCommand).setLastName(lastName);
        ((VerifyOTPCommand)verifyOTPCommand).setRole(role);
        verifyOTPCommand.execute();
        return formulateResponse(verifyOTPCommand);
    }

    @PostMapping("/sendMail")
    public ResponseEntity<Object> sendMail(@RequestParam String subject, @RequestParam String OTP ) {
        ((SendMailCommand)sendMailCommand).setSubject(subject);
        ((SendMailCommand)sendMailCommand).setOTP(OTP);
        sendMailCommand.execute();
        return formulateResponse(sendMailCommand);
    }

    private ResponseEntity<Object> formulateResponse(Command command){
        if(command.getState().toString().equals("FAILURE"))
            return ResponseEntity.badRequest().body(command.getState().getMessage());
        return ResponseEntity.ok(command.getState().getMessage());
    }
}
