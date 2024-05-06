package com.agmadnasfelguc.walgreensreplica.user.service.command.user;

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


    @Override
    public void execute() {
        try {
            String userID = JwtUtil.getUserIdFromToken(sessionID);
            if (userID == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                return;
            }
            Tuple result = userRepository.editUser(UUID.fromString(userID), address, dateOfBirth, gender, phoneNumber, extension);
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            System.out.println(this.getState());
        }
        catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
    }
}