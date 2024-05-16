package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Data
public class UnbanAccountCommand extends Command {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    private String sessionId;

    private String userIdToUnban;

    Logger logger = LoggerFactory.getLogger(UnbanAccountCommand.class);

    @Override
    public void execute() {
        try{
            String role = String.valueOf(sessionCache.getSessionSection(sessionId,"user").get("role"));
            if (role == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
                logger.error(this.getState().getMessage());
                return;
            }
            if (!role.equals("admin")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
                logger.error(this.getState().getMessage());
                return;
            }
            Tuple result = adminRepository.unbanAccount(UUID.fromString(userIdToUnban));
            BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
            this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Success)){
                logger.info("The Ban is lifted"+response.getMessage());
            }else if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error("Unbanning Failed"+response.getMessage());
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            logger.error(e.getMessage());
        }

    }
}
