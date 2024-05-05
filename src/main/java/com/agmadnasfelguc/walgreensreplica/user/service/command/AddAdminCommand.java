package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
@Data

public class AddAdminCommand extends Command {

    private AdminRepository adminRepository;

    private String username;
    private String password;

    @Override
    public void execute() {
        try{
            Tuple response = adminRepository.addAdmin(username,password);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.get(0)), response.get(1) ));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
        }
    }


}