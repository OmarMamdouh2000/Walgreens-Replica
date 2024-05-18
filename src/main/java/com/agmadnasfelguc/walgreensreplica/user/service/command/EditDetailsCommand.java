package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class EditDetailsCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    private String sessionID;
    private String address;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String extension;

    @Autowired
    private SessionCache sessionCache;

    Logger logger = LoggerFactory.getLogger(EditDetailsCommand.class);


    @Override
    public void execute() {
        try {
            String userID = String.valueOf(sessionCache.getSessionSection(sessionID,"user").get("userId"));
            if (userID.equals("null")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                Tuple result = userRepository.editUser(UUID.fromString(userID), address, dateOfBirth, gender, phoneNumber, extension);
                BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            }

        }
        catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }
}