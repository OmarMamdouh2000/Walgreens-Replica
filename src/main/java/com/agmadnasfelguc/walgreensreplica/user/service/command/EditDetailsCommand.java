package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateUserSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class EditDetailsCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    @Setter
    private String sessionId;
    @Setter
    private String address;
    @Setter
    private Date dateOfBirth;
    @Setter
    private String gender;
    @Setter
    private String phoneNumber;
    @Setter
    private String extension;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private GetOrCreateUserSessionCommand userSessionCommand;

    private String userId;



    @Override
    public void execute() {
        try {
            userId = JwtUtil.getUserIdFromToken(sessionId);
            if (userId == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                Tuple result = userRepository.editUser(UUID.fromString(userId), address, dateOfBirth, gender, phoneNumber, extension);
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
                if(this.getState().getStatus().equals(ResponseState.Success)){
                    setUpUserSessionCommandAndExecute();
                    Map<String,Object> sessionDetails = userSessionCommand.getUserMap();
                    if(userSessionCommand.getState().getStatus().equals(ResponseState.Failure)){
                        this.setState(userSessionCommand.getState());
                    }
                    else{
                        if(userSessionCommand.getState().getMessage().equals("Session retrieved")){
                            HashMap<String, Object> updatedDetails = new HashMap<>(sessionDetails);
                            if(address != null){
                                updatedDetails.put("address", address);
                            }
                            if(dateOfBirth != null){
                                updatedDetails.put("date_of_birth", dateOfBirth);
                            }
                            if(gender != null) {
                                updatedDetails.put("gender", gender);
                            }
                            if(phoneNumber != null){
                                updatedDetails.put("phone_number", phoneNumber);
                            }
                            if(extension != null){
                                updatedDetails.put("extension", extension);
                            }
                            sessionCache.updateSessionDetails(sessionId, "user", updatedDetails);
                        }
                    }

                }
            }

        }
        catch (Exception e) {
            ResponseFormulator.formulateException(this, e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

    private void setUpUserSessionCommandAndExecute() {
        userSessionCommand.setSessionId(sessionId);
        userSessionCommand.setUserId(userId);
        userSessionCommand.execute();
    }
}