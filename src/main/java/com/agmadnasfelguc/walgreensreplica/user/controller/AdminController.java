package com.agmadnasfelguc.walgreensreplica.user.controller;

import com.agmadnasfelguc.walgreensreplica.user.service.command.*;
import com.agmadnasfelguc.walgreensreplica.user.service.command.LogoutCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.UserRequests;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.TemplatePaths;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.AdminAddAdmin;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.AdminAddPharmacist;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.AdminLoginRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserRequests userRequests;
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AdminLoginRequest request) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.adminLoginPath, new HashMap<>(), Map.of(Keys.username, request.getUsername(), Keys.password, request.getPassword())));
    }

    @PostMapping("/banAccount")
    public ResponseEntity<Object> banAccount(@RequestParam String sessionId, @RequestParam String userIdToBan) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.banAccountPath, Map.of(Keys.sessionId, sessionId), Map.of(Keys.userId, userIdToBan)));
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam String sessionId) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.logoutPath, Map.of(Keys.sessionId, sessionId), new HashMap<>()));
    }

    @PostMapping("/unbanAccount")
    public ResponseEntity<Object> unbanAccount(@RequestParam String sessionId, @RequestParam String userIdToUnban) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.unbanAccountPath, Map.of(Keys.sessionId, sessionId), Map.of(Keys.userId, userIdToUnban)));
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<Object> addAdmin(@RequestBody AdminAddAdmin request) {
        Map<String,String> paramsMap = Map.of(Keys.sessionId, request.getSessionId());
        Map<String,String> bodyMap = Map.of(Keys.username, request.getUsername(), Keys.password, request.getPassword());
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.addAdminPath, paramsMap, bodyMap));
    }

    @PostMapping("/addPharmacist")
    public ResponseEntity<Object> addPharmacist(@RequestBody AdminAddPharmacist request) {
        Map<String,String> paramsMap = Map.of(Keys.sessionId, request.getSessionId());
        Map<String,String> bodyMap = Map.of(Keys.firstName, request.getFirstName(), Keys.lastName, request.getLastName(), Keys.email, request.getEmail(), Keys.password, request.getPassword());
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.addPharmacistPath, paramsMap, bodyMap));
    }

    @GetMapping("/viewUsers")
    public ResponseEntity<Object> viewUsers(@RequestParam String sessionId) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.viewUsersPath, Map.of(Keys.sessionId, sessionId), new HashMap<>()));
    }

    private ResponseEntity<Object> formulateResponse(JsonNode response) {
        if (response.get("status").asText().equals("Success")) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("status", response.get("status").asText());
            responseBody.put("message", response.get("message").asText());
            JsonNode payload = response.get("payload");
            if (payload != null && !payload.isEmpty()) {
                responseBody.put("payload", payload);
            } else {
                responseBody.put("payload", new HashMap<>());
            }
            return ResponseEntity.ok(responseBody);
        }
        return ResponseEntity.badRequest().body(response.get("message").asText());
    }

}

