package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.*;
import com.agmadnasfelguc.walgreensreplica.user.service.command.LogoutCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final Command loginAdminCommand;
    private final Command banAccountCommand;
    private final Command addAdminCommand;
    private final Command addPharmacistCommand;
    private final Command viewUsersCommand;
    private final Command logoutCommand;
    private final Command unbanAccountCommand;

    @Autowired
    public AdminController(Command loginAdminCommand , Command banAccountCommand , Command addAdminCommand , Command addPharmacistCommand ,Command viewUsersCommand, Command logoutCommand, Command unbanAccountCommand){
        this.loginAdminCommand = loginAdminCommand;
        this.banAccountCommand = banAccountCommand;
        this.addAdminCommand = addAdminCommand;
        this.addPharmacistCommand = addPharmacistCommand;
        this.viewUsersCommand = viewUsersCommand;
        this.logoutCommand = logoutCommand;
        this.unbanAccountCommand = unbanAccountCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password) {
//        try {
            ((LoginAdminCommand) loginAdminCommand).setUsername(username);
            ((LoginAdminCommand) loginAdminCommand).setPassword(password);
            loginAdminCommand.execute();
            if (loginAdminCommand.getState().getStatus().toString().equals("FAILURE"))
                return ResponseEntity.badRequest().body(loginAdminCommand.getState().getMessage());
            return ResponseEntity.ok(loginAdminCommand.getState().getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
//        }
    }

    @PostMapping("/banAccount")
    public ResponseEntity<Object> banAccount(@RequestParam String sessionId, @RequestParam String userIdToBan) {
        ((BanAccountCommand)banAccountCommand).setSessionId(sessionId);
        ((BanAccountCommand)banAccountCommand).setUserIdToBan(userIdToBan);
        banAccountCommand.execute();
        return formulateResponse(banAccountCommand);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam String sessionId) {
        ((LogoutCommand)logoutCommand).setSessionId(sessionId);
        logoutCommand.execute();
        return formulateResponse(logoutCommand);
    }

    @PostMapping("/unbanAccount")
    public ResponseEntity<Object> unbanAccount(@RequestParam String sessionId, @RequestParam String userIdToUnban) {
        ((UnbanAccountCommand)unbanAccountCommand).setSessionId(sessionId);
        ((UnbanAccountCommand)unbanAccountCommand).setUserIdToUnban(userIdToUnban);
        unbanAccountCommand.execute();
        return formulateResponse(unbanAccountCommand);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<Object> addAdmin(@RequestParam String username ,@RequestParam String password, @RequestParam String sessionId) {
        ((AddAdminCommand)addAdminCommand).setUsername(username);
        ((AddAdminCommand)addAdminCommand).setPassword(password);
        ((AddAdminCommand)addAdminCommand).setSessionId(sessionId);
        addAdminCommand.execute();
        return formulateResponse(addAdminCommand);
    }

    @PostMapping("/addPharmacist")
    public ResponseEntity<Object> addPharmacist(@RequestParam String firstName , @RequestParam String lastName , @RequestParam String email , @RequestParam String password, @RequestParam String sessionId) {
        ((AddPharmacistCommand)addPharmacistCommand).setFirstName(firstName);
        ((AddPharmacistCommand)addPharmacistCommand).setLastName(lastName);
        ((AddPharmacistCommand)addPharmacistCommand).setEmail(email);
        ((AddPharmacistCommand)addPharmacistCommand).setPassword(password);
        ((AddPharmacistCommand)addPharmacistCommand).setSessionId(sessionId);
        addPharmacistCommand.execute();
        return formulateResponse(addPharmacistCommand);
    }

    @GetMapping("/viewUsers")
    public ResponseEntity<List<Object>> viewUsers(@RequestParam String sessionId) {
        ((ViewUsersCommand)viewUsersCommand).setSessionId(sessionId);
        viewUsersCommand.execute();
        if(viewUsersCommand.getState().getStatus().toString().equals("FAILURE"))
            return ResponseEntity.badRequest().body(Collections.singletonList(viewUsersCommand.getState().getMessage()));
        return ResponseEntity.ok(Collections.singletonList(((ViewUsersCommand) viewUsersCommand).getUserInfo()));
    }

    private ResponseEntity<Object> formulateResponse(Command command){
        if(command.getState().getStatus().toString().equals("FAILURE"))
            return ResponseEntity.badRequest().body(command.getState().getMessage());
        return ResponseEntity.ok(command.getState().getMessage());
    }

}

