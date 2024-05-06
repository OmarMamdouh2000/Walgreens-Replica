package com.agmadnasfelguc.walgreensreplica.user.service.command.admin;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class BanAccountCommand extends Command {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    private String sessionId;

    private String userIdToBan;

    @Override
    public void execute() {
        try{
            String role = sessionCache.getSessionDetails(sessionId).get("role");
            if (role == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                return;
            }
            if (!role.equals("admin")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
                return;
            }
            Tuple result = adminRepository.banAccount(UUID.fromString(userIdToBan));
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
    }


}