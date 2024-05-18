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
public class AddPharmacistCommand extends Command {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private SessionCache sessionCache;

    private String sessionId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    Logger logger = LoggerFactory.getLogger(AddPharmacistCommand.class);

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
                }
                else {
                    String response = adminRepository.addPharmacist(firstName,lastName,email,password);
                    this.setState(new ResponseStatus(ResponseState.Success, response));
                }
            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }


}