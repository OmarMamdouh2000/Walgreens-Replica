package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Data
public class EditDetailsCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    private String sessionID;
    private String address;
    private Date dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String extension;


    @Override
    public void execute() {
        try {
            String userID = JwtUtil.getUserIdFromToken(sessionID);
            if (userID == null) {
                this.setState(new ResponseStatus(ResponseState.FAILURE, "Invalid Session"));
                return;
            }
            Tuple result = userRepository.editUser(UUID.fromString(userID), address, dateOfBirth, gender, phoneNumber, extension);
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
        }
        catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }
    }
}