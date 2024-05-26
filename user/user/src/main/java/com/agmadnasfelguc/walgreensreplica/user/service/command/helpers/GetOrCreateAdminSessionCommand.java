package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.model.Admin;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
@Slf4j
@Service
public class GetOrCreateAdminSessionCommand extends Command {
    @Setter
    private String sessionId;
    @Setter
    private String userId;
    @Getter
    Map<String, Object> adminMap;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;


    @Override
    public void execute() {
        try {
            if ((adminMap = sessionCache.getAdminSessionDetails(sessionId)) != null) {
                this.setState(new ResponseStatus(ResponseState.Success, "Session retrieved"));
                return;
            }
            Optional<Admin> adminOptional = adminRepository.findById(UUID.fromString(userId));
            if(adminOptional.isPresent()){
                adminMap = sessionCache.createAdminSession(sessionId, userId);
                this.setState(new ResponseStatus(ResponseState.Success, "Session created"));
            }
            else{
                this.setState(new ResponseStatus(ResponseState.Failure, "Admin not found"));
            }
        }
        catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateLogger(log,this.getState());
    }

}
