package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class ViewUsersCommand extends Command {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    private String sessionId;

    private List<Object> userInfo;

    Logger logger = LoggerFactory.getLogger(ViewUsersCommand.class);

    @Override
    public void execute() {
        try{
            logger.info("View Users Command Execute");
            Map<String, Object> details = sessionCache.getAdminSessionDetails(sessionId);
            String role = String.valueOf(details.get("role"));
            if (!role.equals("admin")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session or Session Type"));
            } else {
                List<Tuple> rawUsers = adminRepository.getAllUsers();
                userInfo = rawUsers.stream().map(tuple -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("userId", tuple.get("user_id"));
                    userMap.put("email", tuple.get("email"));
                    userMap.put("role", tuple.get("role"));
                    userMap.put("status", tuple.get("status"));
                    userMap.put("emailVerified", tuple.get("email_verified"));
                    userMap.put("TwoFactorAuthEnabled", tuple.get("TwoFactorAuth_Enabled"));
                    userMap.put("firstName", tuple.get("first_name"));
                    userMap.put("lastName", tuple.get("last_name"));
                    if(!tuple.get("address").equals("")){
                        userMap.put("address", tuple.get("address"));
                    }
                    java.sql.Date sqlDateOfBirth = tuple.get("date_of_birth", java.sql.Date.class); // Safely retrieve the SQL Date
                    if (sqlDateOfBirth != null) {
                        java.util.Date utilDateOfBirth = new java.util.Date(sqlDateOfBirth.getTime());
                        String isoDateOfBirth = new SimpleDateFormat("yyyy-MM-dd").format(utilDateOfBirth);
                        userMap.put("dateOfBirth", isoDateOfBirth);
                    }
                    if(tuple.get("gender")!=null){
                        userMap.put("gender", tuple.get("gender"));
                    }
                    if(!tuple.get("phone_number").equals("")){
                        userMap.put("phoneNumber", tuple.get("phone_number"));
                    }
                    if(!tuple.get("extension").equals("")){
                        userMap.put("extension", tuple.get("extension"));
                    }
                    return userMap;
                }).collect(Collectors.toList());
                this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        if(this.getState().getStatus().equals(ResponseState.Success)){
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("users", userInfo));
        }
        else{
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
        }
    }
}

