package com.agmadnasfelguc.walgreensreplica.user.service.command.admin;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    @Override
    public void execute() {
//        try{
        System.out.println(sessionId);
        Map<String,String> details = sessionCache.getSessionDetails(sessionId);
        System.out.println(details);
        String role = details.get("role");
        if(role == null){
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            return;
        }
        if(!role.equals("admin")){
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
            return;
        }
        userInfo = userRepository.findAll();
        System.out.println(userInfo);
        this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));
//        } catch (Exception e) {
//            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getMessage()));
//        }
    }
}

