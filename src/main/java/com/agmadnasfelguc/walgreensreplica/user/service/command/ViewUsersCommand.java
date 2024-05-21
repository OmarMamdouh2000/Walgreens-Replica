package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.ViewUserResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.ViewUserResult;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateAdminSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Service
public class ViewUsersCommand extends Command {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private GetOrCreateAdminSessionCommand adminSessionCommand;

    @Setter
    private String sessionId;

    @Getter
    private List<ViewUserResult> userInfo = new ArrayList<>();

    private String userId;

    @Override
    public void execute() {
        try {
            userId = JwtUtil.getUserIdFromToken(sessionId);
            if(userId == null){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else {
                setUpAdminSessionCommandAndExecute();
                if (adminSessionCommand.getState().getStatus().equals(ResponseState.Failure)) {
                    this.setState(adminSessionCommand.getState());
                } else {
                    List<Tuple> rawUsers = adminRepository.getAllUsers();
                    rawUsers.forEach(tuple -> {
                        Map<String, Object> tupleMap = new HashMap<>();
                        tuple.getElements().forEach(element -> tupleMap.put(element.getAlias(), tuple.get(element)));
                        if(tupleMap.get("image_id") != null)
                            tupleMap.put("image_url", firebaseService.getPhotoUrl(String.valueOf(tupleMap.get("image_id"))));
                        userInfo.add(ViewUserResultConverter.convertTupleToDTO(tupleMap));
                    });
                    if(!userInfo.isEmpty()){
                        this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));
                    }
                    else{
                        this.setState(new ResponseStatus(ResponseState.Success, "No users found"));
                    }
                }
            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this,e);
        }
        if(this.getState().getStatus().equals(ResponseState.Success))
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("users",userInfo));
        else
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

    private void setUpAdminSessionCommandAndExecute(){
        adminSessionCommand.setSessionId(sessionId);
        adminSessionCommand.setUserId(userId);
        adminSessionCommand.execute();
    }
}
