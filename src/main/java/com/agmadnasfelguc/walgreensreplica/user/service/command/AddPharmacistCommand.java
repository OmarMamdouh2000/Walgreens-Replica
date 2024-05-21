package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.PasswordHasher;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateAdminSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@EqualsAndHashCode(callSuper = true)
@Service
@Slf4j
public class AddPharmacistCommand extends Command {

    @Autowired
    private AdminRepository adminRepository;

    @Setter
    private String sessionId;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String email;
    @Setter
    private String password;

    @Autowired
    private GetOrCreateAdminSessionCommand adminSessionCommand;

    private String userId;

    @Override
    public void execute() {
        try{
            userId = JwtUtil.getUserIdFromToken(sessionId);
            if(userId == null){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                setUpAdminSessionCommandAndExecute();
                if(adminSessionCommand.getState().getStatus().equals(ResponseState.Failure)){
                    this.setState(adminSessionCommand.getState());
                }
                else{
                    String dbState = adminRepository.addPharmacist(firstName, lastName, email, PasswordHasher.hashPassword(password));
                    this.setState(new ResponseStatus(ResponseState.Success, dbState));
                }

            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

    private void setUpAdminSessionCommandAndExecute(){
        adminSessionCommand.setSessionId(sessionId);
        adminSessionCommand.setUserId(userId);
        adminSessionCommand.execute();
    }


}