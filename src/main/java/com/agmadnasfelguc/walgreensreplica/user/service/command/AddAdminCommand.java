package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    @Override
    public void execute() {
//        try{
        String role = sessionCache.getSessionDetails(sessionId).get("role");
        if (role == null) {
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            return;
        }
        if (!role.equals("admin")) {
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session Type"));
            return;
        }
        String response = adminRepository.addAdmin(username,password);
        this.setState(new ResponseStatus(ResponseState.Success, response));
//        } catch (Exception e) {
//            this.setState(new ResponseStatus(ResponseState.FAILURE, e.getStackTrace()));
//        }
    }


}