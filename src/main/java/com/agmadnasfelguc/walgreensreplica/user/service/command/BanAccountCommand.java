package com.agmadnasfelguc.walgreensreplica.user.service.command;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(BanAccountCommand.class);
    @Autowired
    private ResetPasswordCommand resetPasswordCommand;

    @Override
    public void execute() {
        try{
            String role = sessionCache.getSessionDetails(sessionId).get("role");
            if (role == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                if(this.getState().getStatus().equals(ResponseState.Failure)){
                    logger.error(this.getState().getMessage());
                }
                return;
            }
            if (!role.equals("admin")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
                if(this.getState().getStatus().equals(ResponseState.Failure)){
                    logger.error(this.getState().getMessage());
                }
                return;
            }
            Tuple result = adminRepository.banAccount(UUID.fromString(userIdToBan));
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("Account Got banned" + response.getMessage());
            }
            else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Banning the account failed" +response.getMessage());
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error(e.getMessage());
            }
        }
    }


}