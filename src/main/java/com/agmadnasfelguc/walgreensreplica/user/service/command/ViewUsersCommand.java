package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
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
        Map<String, Object> details = sessionCache.getAdminSessionDetails(sessionId);
        String role = String.valueOf(details.get("role"));
        if(role.equals("null")){
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
        }
        else{
            if(!role.equals("admin")){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
            }
            else{
                userInfo = userRepository.findAll();
                this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));
            }

        }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        if(this.getState().getStatus().equals(ResponseState.Failure)){
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("users", userInfo));
        }
    }
}

