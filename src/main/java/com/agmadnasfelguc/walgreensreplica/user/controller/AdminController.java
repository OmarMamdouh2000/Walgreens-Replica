package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.LoginAdminCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final Command loginAdminCommand;

    @Autowired
    public AdminController(Command loginAdminCommand) {
        this.loginAdminCommand = loginAdminCommand;
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
}

