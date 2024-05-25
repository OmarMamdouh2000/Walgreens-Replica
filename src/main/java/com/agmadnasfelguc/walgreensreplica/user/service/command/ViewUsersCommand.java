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

import java.util.*;
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
            String hashedKey = JwtUtil.getUserIdFromToken(sessionId);
            if(hashedKey == null || !hashedKey.contains(":") || !hashedKey.split(":")[1].equals("admin")){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                userId = hashedKey.split(":")[0];
                setUpAdminSessionCommandAndExecute();
                if (adminSessionCommand.getState().getStatus().equals(ResponseState.Failure)) {
                    this.setState(adminSessionCommand.getState());
                } else {
                    List<UUID> allUserIds = adminRepository.getAllUserIds();
                    List<UUID> cachedUserIds = sessionCache.getKeysContainingUser();
                    for(UUID id : cachedUserIds){
                        Map<String, Object> sessionDetails = sessionCache.getSessionSection(id.toString(),"");
                        userInfo.add(ViewUserResultConverter.convertTupleToDTO(sessionDetails));
                    }
                    UUID[] userIdsToGet = allUserIds.stream().filter(id -> !cachedUserIds.contains(id)).toList().toArray(new UUID[0]);
                    if(userIdsToGet.length!=0) {
                        List<Tuple> rawUsers = adminRepository.getAllUsers(userIdsToGet);
                        rawUsers.forEach(tuple -> {
                            Map<String, Object> tupleMap = new HashMap<>();
                            tuple.getElements().forEach(element -> tupleMap.put(element.getAlias(), tuple.get(element)));
                            if (tupleMap.get("image_id") != null)
                                tupleMap.put("image_url", firebaseService.getPhotoUrl(String.valueOf(tupleMap.get("image_id"))));
                            userInfo.add(ViewUserResultConverter.convertTupleToDTO(tupleMap));
                        });
                    }
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
