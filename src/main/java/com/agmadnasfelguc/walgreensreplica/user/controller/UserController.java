package com.agmadnasfelguc.walgreensreplica.user.controller;
import com.agmadnasfelguc.walgreensreplica.user.service.command.*;
import com.agmadnasfelguc.walgreensreplica.user.service.command.common.LogoutCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.user.*;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserChangeEmailRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserChangePasswordRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserEditRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserRegistrationRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final Command registerCommand;
    private final Command changeEmailCommand;
    private final Command changePasswordCommand;
    private final Command editDetailsCommand;

    private final Command loginUserCommand;
    private final Command logoutCommand;
    private final Command twoFactorAuthLoginCommand;
    private final Command resetPasswordCommand;

    private final Command verifyMailCommand;

    private final Command update2FAStatusCommand;

    private final Command verifyMailCheckOTPCommand;

    private final Command updatePasswordResetCommand;

    @Autowired
    public UserController(Command registerCommand, Command changeEmailCommand, Command changePasswordCommand, Command editDetailsCommand, Command loginUserCommand, Command logoutCommand, Command twoFactorAuthLoginCommand, Command resetPasswordCommand, Command verifyMailCommand, Command update2FAStatusCommand, Command verifyMailCheckOTPCommand, Command updatePasswordResetCommand) {
        this.registerCommand = registerCommand;
        this.changeEmailCommand = changeEmailCommand;
        this.changePasswordCommand = changePasswordCommand;
        this.editDetailsCommand = editDetailsCommand;
        this.loginUserCommand = loginUserCommand;
        this.logoutCommand = logoutCommand;
        this.twoFactorAuthLoginCommand = twoFactorAuthLoginCommand;
        this.resetPasswordCommand = resetPasswordCommand;
        this.verifyMailCommand = verifyMailCommand;
        this.update2FAStatusCommand = update2FAStatusCommand;
        this.verifyMailCheckOTPCommand = verifyMailCheckOTPCommand;
        this.updatePasswordResetCommand = updatePasswordResetCommand;
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
    public ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordRequest request) {
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
        ((EditDetailsCommand)editDetailsCommand).setDateOfBirth(request.formatDate());
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
        if(loginUserCommand.getState().getStatus().toString().equals("FAILURE"))
            return ResponseEntity.badRequest().body(loginUserCommand.getState().getMessage());
        return ResponseEntity.ok(loginUserCommand.getState().getMessage());

    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam String sessionId) {
        ((LogoutCommand)logoutCommand).setSessionId(sessionId);
        logoutCommand.execute();
        return formulateResponse(logoutCommand);
    }

    @PostMapping("/twoFactorAuthLogin")
    public ResponseEntity<Object> twoFactorAuthLogin(@RequestParam String email, @RequestParam String OTP, @RequestParam String userId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String role) {
        ((TwoFactorAuthLoginCommand)twoFactorAuthLoginCommand).setEmail(email);
        ((TwoFactorAuthLoginCommand)twoFactorAuthLoginCommand).setOtp(OTP);
        ((TwoFactorAuthLoginCommand)twoFactorAuthLoginCommand).setUserId(userId);
        ((TwoFactorAuthLoginCommand)twoFactorAuthLoginCommand).setFirstName(firstName);
        ((TwoFactorAuthLoginCommand)twoFactorAuthLoginCommand).setLastName(lastName);
        ((TwoFactorAuthLoginCommand)twoFactorAuthLoginCommand).setRole(role);
        twoFactorAuthLoginCommand.execute();
        return formulateResponse(twoFactorAuthLoginCommand);
    }

    @PostMapping("/verifyMailCheckOtp")
    public ResponseEntity<Object> verifyMailCheckOtp(@RequestParam String sessionId, @RequestParam String otp) {
        ((VerifyMailCheckOTPCommand)verifyMailCheckOTPCommand).setSessionId(sessionId);
        ((VerifyMailCheckOTPCommand)verifyMailCheckOTPCommand).setOtp(otp);
        verifyMailCheckOTPCommand.execute();
        return formulateResponse(verifyMailCheckOTPCommand);
    }

    @PostMapping("/updatePasswordReset")
    public ResponseEntity<Object> updatePasswordReset(@RequestParam String email, @RequestParam String password, @RequestParam String otp) {
        ((UpdatePasswordResetCommand)updatePasswordResetCommand).setEmail(email);
        ((UpdatePasswordResetCommand)updatePasswordResetCommand).setPassword(password);
        ((UpdatePasswordResetCommand)updatePasswordResetCommand).setOtp(otp);
        updatePasswordResetCommand.execute();
        return formulateResponse(updatePasswordResetCommand);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestParam String email) {
        ((ResetPasswordCommand)resetPasswordCommand).setEmail(email);
        resetPasswordCommand.execute();
        return formulateResponse(resetPasswordCommand);
    }

    @PostMapping("/verifyMail")
    public ResponseEntity<Object> verifyMail(@RequestParam String sessionId) {
        ((VerifyMailCommand)verifyMailCommand).setSessionId(sessionId);
        verifyMailCommand.execute();
        return formulateResponse(verifyMailCommand);
    }

    @PostMapping("/update2FAStatus")
    public ResponseEntity<Object> update2FAStatus(@RequestParam String sessionId, @RequestParam boolean status) {
        ((Update2FAStatusCommand)update2FAStatusCommand).setSessionId(sessionId);
        ((Update2FAStatusCommand)update2FAStatusCommand).setTwoFAEnabled(status);
        update2FAStatusCommand.execute();
        return formulateResponse(update2FAStatusCommand);
    }

    private ResponseEntity<Object> formulateResponse(Command command){
        if(command.getState().getStatus().equals(ResponseState.Failure))
            return ResponseEntity.badRequest().body(command.getState().getMessage());
        return ResponseEntity.ok(command.getState().getMessage());
    }
}
