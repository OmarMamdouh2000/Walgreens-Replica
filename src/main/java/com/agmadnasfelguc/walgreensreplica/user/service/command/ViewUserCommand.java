package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.ViewUserResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.ViewUserResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class ViewUserCommand extends Command{

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseService firebaseService;

    private String sessionId;

    private ViewUserResult userInfo;

    @Override
    public void execute() {
        try {
            log.info("View User Command Execute");
            String userID = String.valueOf(sessionCache.getSessionSection(sessionId,"user").get("userId"));
            if(userID.equals("null")){
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            }
            else{
                Tuple rawUser = userRepository.getUser(UUID.fromString(userID));
                Map<String, Object> tupleMap = new HashMap<>();
                rawUser.getElements().forEach(element -> tupleMap.put(element.getAlias(), rawUser.get(element)));
                tupleMap.put("image_url", firebaseService.getPhotoUrl(String.valueOf(tupleMap.get("image_id"))));
                userInfo = ViewUserResultConverter.convertTupleToDTO(tupleMap);
                this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));
            }

        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        if(this.getState().getStatus().equals(ResponseState.Success))
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("user",userInfo));
        else
            ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }

}
