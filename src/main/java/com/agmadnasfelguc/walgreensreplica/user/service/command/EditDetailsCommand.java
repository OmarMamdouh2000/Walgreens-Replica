package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
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

    Logger logger = LoggerFactory.getLogger(EditDetailsCommand.class);


    @Override
    public void execute() {
        try {
            String userID = JwtUtil.getUserIdFromToken(sessionID);
            if (userID == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                if(this.getState().getStatus().equals(ResponseState.Failure)){
                    logger.error(this.getState().getMessage());
                }
                return;
            }
            Tuple result = userRepository.editUser(UUID.fromString(userID), address, dateOfBirth, gender, phoneNumber, extension);
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("The details changed" + response.getMessage());
            }
            else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Changing the details failed" +response.getMessage());
            }
        }
        catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error(e.getMessage());
            }
        }
    }
}