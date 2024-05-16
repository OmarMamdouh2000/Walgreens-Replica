package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
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
        String response = adminRepository.addAdmin(username,password);
        this.setState(new ResponseStatus(ResponseState.Success, response));
        if(this.getState().getStatus().equals(ResponseState.Success)){
            logger.info("Admin added successfully" + this.getState().getMessage());
        }
        else if(this.getState().getStatus().equals(ResponseState.Failure)){
            logger.error("Adding admin failed" + this.getState().getMessage());
        }

        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            if(this.getState().getStatus().equals(ResponseState.Failure)){
                logger.error(e.getMessage());
            }

        }
    }


}