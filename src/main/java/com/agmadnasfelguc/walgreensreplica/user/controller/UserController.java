package com.agmadnasfelguc.walgreensreplica.user.controller;
import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.UserRequests;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.TemplatePaths;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserChangeEmailRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserChangePasswordRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserEditRequest;
import com.agmadnasfelguc.walgreensreplica.user.service.requests.UserRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRequests userRequests;

    @Autowired
    private FirebaseService firebaseService;

    private int i=0;

    @GetMapping("/test")
    public ResponseEntity<Object> test() throws ExecutionException, InterruptedException, JsonProcessingException {
        System.out.println(i++);
        return ResponseEntity.ok(i);
    }
    @PostMapping("/registerUser")
    public ResponseEntity<Object> registerUser(@RequestBody UserRegistrationRequest request) {
        Map<String,String> bodyMap = Map.of("email", request.getEmail(), "password", request.getPassword(), "firstName", request.getFirstName(), "lastName", request.getLastName());
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.registerPath,  new HashMap<>(), bodyMap));
    }


    @PostMapping("/changeEmail")
    public ResponseEntity<Object> changeEmail(@RequestBody UserChangeEmailRequest request) {

        Map<String,String> paramsMap = Map.of(Keys.sessionId, request.getSessionId());
        Map<String,String> bodyMap = Map.of(Keys.newEmail, request.getEmail(), Keys.password, request.getPassword());
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.changeEmailPath, paramsMap, bodyMap));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordRequest request) {

        Map<String,String> paramsMap = Map.of(Keys.sessionId, request.getSessionId());
        Map<String,String> bodyMap = Map.of(Keys.oldPassword, request.getOldPassword(), Keys.newPassword, request.getNewPassword());
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.changePasswordPath, paramsMap, bodyMap));
    }


    @PostMapping("/editPersonalDetails")
    public ResponseEntity<Object> editPersonalDetails(@RequestBody UserEditRequest request) {
        Map<String,String> paramsMap = Map.of(Keys.sessionId, request.getSessionId());
        Map<String, String> bodyMap = new HashMap<>();

        if (request.getDateOfBirth() != null) {
            bodyMap.put(Keys.dateOfBirth, request.getDateOfBirth());
        }
        if (request.getGender() != null) {
            bodyMap.put(Keys.gender, request.getGender());
        }
        if (request.getPhoneNumber() != null) {
            bodyMap.put(Keys.phoneNumber, request.getPhoneNumber());
        }
        if (request.getExtension() != null) {
            bodyMap.put(Keys.extension, request.getExtension());
        }
        if (request.getAddress() != null) {
            bodyMap.put(Keys.address, request.getAddress());
        }
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.editDetailsPath, paramsMap, bodyMap));

    }
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
        Map<String ,String> bodyMap = Map.of(Keys.email, email, Keys.password, password);
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.userLoginPath, new HashMap<>(), bodyMap));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestParam String sessionId) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.logoutPath, Map.of(Keys.sessionId, sessionId), new HashMap<>()));
    }

    @PostMapping("/twoFactorAuthLogin")
    public ResponseEntity<Object> twoFactorAuthLogin(@RequestParam String email, @RequestParam String otp, @RequestParam String userId, @RequestParam String firstName, @RequestParam String lastName, @RequestParam String role, @RequestParam String emailVerified) {
        Map<String,String> paramsMap = Map.of(Keys.userId, userId);
        Map<String,String> bodyMap = Map.of(Keys.email, email, Keys.otp, otp, Keys.firstName, firstName, Keys.lastName, lastName, Keys.role, role, Keys.emailVerified, emailVerified);
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.twoFactorAuthLoginPath, paramsMap, bodyMap));
    }

    @PostMapping("/verifyMailCheckOtp")
    public ResponseEntity<Object> verifyMailCheckOtp(@RequestParam String sessionId, @RequestParam String otp) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.verifyMailCheckOtpPath, Map.of(Keys.sessionId, sessionId), Map.of(Keys.otp, otp)));
    }

    @PostMapping("/updatePasswordReset")
    public ResponseEntity<Object> updatePasswordReset(@RequestParam String email, @RequestParam String password, @RequestParam String otp) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.resetPasswordCheckOtpPath, new HashMap<>(), Map.of(Keys.email, email, Keys.newPassword, password, Keys.otp, otp)));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Object> resetPassword(@RequestParam String email) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.resetPasswordPath, new HashMap<>(), Map.of(Keys.email, email)));
    }

    @PostMapping("/verifyMail")
    public ResponseEntity<Object> verifyMail(@RequestParam String sessionId) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.verifyMailPath, Map.of(Keys.sessionId, sessionId), new HashMap<>()));
    }

    @PostMapping("/update2FAStatus")
    public ResponseEntity<Object> update2FAStatus(@RequestParam String sessionId, @RequestParam boolean status) {
        return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.update2FAStatusPath, Map.of(Keys.sessionId, sessionId), Map.of(Keys.status, String.valueOf(status))));
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<Object> uploadImage(@RequestParam String sessionId, @RequestParam("imageFile") MultipartFile imageFile){
        UUID imageId = UUID.randomUUID();
        try {
            firebaseService.uploadPhoto(imageId.toString(), imageFile);
            logger.info("Image uploaded to firebase");
            return formulateResponse(userRequests.sendAndReceiveRequest(TemplatePaths.uploadImagePath, Map.of(Keys.sessionId, sessionId), Map.of(Keys.imageId, imageId.toString())));
        } catch (IOException e) {
            logger.error("Failed to upload image to firebase: ", e);
            return ResponseEntity.badRequest().body("Failed to upload image");
        }
    }

    private ResponseEntity<Object> formulateResponse(JsonNode response) {
        if (response.get("status").asText().equals("Success")||response.get("status").asText().equals("Pending")) {
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
