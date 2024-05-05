package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final Command loginAdminCommand;
    private final Command banAccountCommand;
    private final Command addAdminCommand;
    private final Command addPharmacistCommand;
    private final Command viewUsersCommand;

    @Autowired
    public AdminController(Command loginAdminCommand , Command banAccountCommand , Command addAdminCommand , Command addPharmacistCommand ,Command viewUsersCommand) {
        this.loginAdminCommand = loginAdminCommand;
        this.banAccountCommand = banAccountCommand;
        this.addAdminCommand = addAdminCommand;
        this.addPharmacistCommand = addPharmacistCommand;
        this.viewUsersCommand = viewUsersCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String username, @RequestParam String password) {
        try {
            ((LoginAdminCommand) loginAdminCommand).setUsername(username);
            ((LoginAdminCommand) loginAdminCommand).setPassword(password);
            loginAdminCommand.execute();
            if (loginAdminCommand.getState().toString().equals("FAILURE"))
                return ResponseEntity.badRequest().body(loginAdminCommand.getState().getMessage());
            return ResponseEntity.ok(loginAdminCommand.getState().getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/banAccount")
    public ResponseEntity<Object> banAccount(@RequestParam String userId ) {
        ((banAccountCommand)banAccountCommand).setUserId(userId);
        banAccountCommand.execute();
        return formulateResponse(banAccountCommand);
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<Object> addAdmin(@RequestParam String username ,@RequestParam String password ) {
        ((addAdminCommand)addAdminCommand).setUserId(userId);
        addAdminCommand.execute();
        return formulateResponse(addAdminCommand);
    }

    @PostMapping("/addPharmacist")
    public ResponseEntity<Object> addPharmacist(@RequestParam String firstName , @RequestParam String lastName , @RequestParam String email , @RequestParam String password ) {
        ((addPharmacistCommand)addPharmacistCommand).setFirstName(userId);
        ((addPharmacistCommand)addPharmacistCommand).setLastName(userId);
        ((addPharmacistCommand)addPharmacistCommand).setEmail(userId);
        ((addPharmacistCommand)addPharmacistCommand).setPassword(userId);
        addPharmacistCommand.execute();
        return formulateResponse(addPharmacistCommand);
    }

    @GetMapping("/viewUsers")
    public List<User> viewUsers() {
        return viewUsersCommand.execute();
    }

    private ResponseEntity<Object> formulateResponse(Command command){
        if(command.getState().toString().equals("FAILURE"))
            return ResponseEntity.badRequest().body(command.getState().getMessage());
        return ResponseEntity.ok(command.getState().getMessage());
    }

}

