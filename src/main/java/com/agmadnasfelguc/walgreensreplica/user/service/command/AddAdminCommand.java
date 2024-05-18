package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class AddAdminCommand extends Command {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SessionCache sessionCache;

    private String sessionId;

    private String username;
    private String password;

    Logger logger = LoggerFactory.getLogger(AddAdminCommand.class);

    @Override
    public void execute() {
       try{
           String role = String.valueOf(sessionCache.getAdminSessionDetails(sessionId).get("role"));
        if (role.equals("null")) {
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
        }
        else{
            if (!role.equals("admin")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
                getUserRequests().publishResponse(this.getReplyTopic(), this.getCorrelationId(), this.getState(), null);
            }else{
                String response = adminRepository.addAdmin(username,password);
                this.setState(new ResponseStatus(ResponseState.Success, response));
            }
        }

        } catch (Exception e) {
           e.printStackTrace();
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }


}