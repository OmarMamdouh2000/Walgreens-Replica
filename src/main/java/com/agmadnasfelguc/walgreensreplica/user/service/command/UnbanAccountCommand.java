package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.BasicResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.BasicResult;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateAdminSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnbanAccountCommand extends Command {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    @Setter
    private String sessionId;

    @Setter
    private String userIdToUnban;

    private String userId;
    @Autowired
    private GetOrCreateAdminSessionCommand adminSessionCommand;

    Logger logger = LoggerFactory.getLogger(UnbanAccountCommand.class);

    @Override
    public void execute() {
        try{
            String hashedKey = JwtUtil.getUserIdFromToken(sessionId);
            if(hashedKey == null || !hashedKey.contains(":") || !hashedKey.split(":")[1].equals("admin")){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                userId = hashedKey.split(":")[0];
                setUpAdminSessionCommandAndExecute();
                if (adminSessionCommand.getState().getStatus().equals(ResponseState.Failure)) {
                    this.setState(adminSessionCommand.getState());
                } else {
                    Tuple result = adminRepository.unbanAccount(UUID.fromString(userIdToUnban));
                    BasicResult response = BasicResultConverter.convertTupleToBasicResult(result);
                    this.setState(new ResponseStatus(ResponseState.valueOf(response.getStatus()), response.getMessage()));
                }
            }

        } catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }
    private void setUpAdminSessionCommandAndExecute() {
        adminSessionCommand.setSessionId(sessionId);
        adminSessionCommand.setUserId(userId);
        adminSessionCommand.execute();
    }
}
