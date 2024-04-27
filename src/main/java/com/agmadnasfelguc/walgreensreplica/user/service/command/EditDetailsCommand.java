package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.model.enums.Gender;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditDetailsCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    private String userID;
    private String address;
    private Date dateOfBirth;
    private Gender gender;
    private String phoneNumber;
    private String extension;


    @Override
    public void execute() {
        try {
            List<String> response = userRepository.editUser(userID, address, dateOfBirth, gender, phoneNumber, extension);
            System.out.println(response);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.get(0)), response.get(1)));
        }
        catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }
    }
}