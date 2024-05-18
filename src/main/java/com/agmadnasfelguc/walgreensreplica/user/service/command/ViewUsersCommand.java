package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.repository.AdminRepository;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.ViewUserResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.ViewUserResult;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Service
@Data
public class ViewUsersCommand extends Command {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SessionCache sessionCache;

    @Autowired
    private FirebaseService firebaseService;

    private String sessionId;

    private List<ViewUserResult> userInfo;

    Logger logger = LoggerFactory.getLogger(ViewUsersCommand.class);

    @Override
    public void execute() {
        try {
            logger.info("View Users Command Execute");
            Map<String, Object> details = sessionCache.getAdminSessionDetails(sessionId);
            String role = String.valueOf(details.get("role"));
            if (!role.equals("admin")) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session or Session Type"));
            } else {
                List<Tuple> rawUsers = adminRepository.getAllUsers();
                rawUsers.forEach(tuple -> {
                    Map<String, Object> tupleMap = new HashMap<>();
                    tuple.getElements().forEach(element -> tupleMap.put(element.getAlias(), tuple.get(element)));
                    tupleMap.put("image_url", firebaseService.getPhotoUrl(String.valueOf(tupleMap.get("image_id"))));
                    userInfo.add(ViewUserResultConverter.convertTupleToDTO(tupleMap));
                });
                if(userInfo != null){
                    this.setState(new ResponseStatus(ResponseState.Success, "Users retrieved successfully"));

                }
                else{
                    this.setState(new ResponseStatus(ResponseState.Success, "No users found"));
                }

            }
        } catch (Exception e) {
            this.setState(new ResponseStatus(ResponseState.Failure, e.getMessage()));
        }
        if(this.getState().getStatus().equals(ResponseState.Success))
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("users",userInfo));
        else
            ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), null);
    }
}
