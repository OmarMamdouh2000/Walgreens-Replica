package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class ViewUsersCommand extends Command {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionCache sessionCache;

    private String sessionId;

    private List<User> userInfo;

    Logger logger = LoggerFactory.getLogger(ViewUsersCommand.class);

    @Override
    public void execute() {
        try{
        System.out.println(sessionId);
        Map<String,String> details = sessionCache.getSessionDetails(sessionId);
        System.out.println(details);
        String role = details.get("role");
        if(role == null){
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            logger.error(this.getState().getMessage());
            return;
        }
        if(!role.equals("admin")){
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
            logger.error(this.getState().getMessage());
            return;
        }
        userInfo = userRepository.findAll();
        System.out.println(userInfo);
        this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));
        if(this.getState().getStatus().equals(ResponseState.Success)){
            logger.info(this.getState().getMessage());
        }
        else if(this.getState().getStatus().equals(ResponseState.Failure)){
            logger.error("Error retrieving users");
        }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
            logger.error(e.getMessage());
        }
    }
}

