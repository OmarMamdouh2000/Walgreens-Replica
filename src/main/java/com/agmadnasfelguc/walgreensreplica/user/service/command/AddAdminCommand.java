package com.agmadnasfelguc.walgreensreplica.user.service.command;


import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.PasswordHasher;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateAdminSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Service
public class AddAdminCommand extends Command {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private GetOrCreateAdminSessionCommand adminSessionCommand;

    @Setter
    private String sessionId;
    @Setter
    private String username;
    @Setter
    private String password;

    private String userId;

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
                if(adminSessionCommand.getState().getStatus().equals(ResponseState.Failure)){
                    this.setState(adminSessionCommand.getState());
                }
                else{
                    String dbState = adminRepository.addAdmin(username, PasswordHasher.hashPassword(password));
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