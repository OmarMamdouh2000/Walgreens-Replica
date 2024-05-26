package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.repository.Converters.ViewUserResultConverter;
import com.agmadnasfelguc.walgreensreplica.user.repository.ResultSetsMapping.ViewUserResult;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import jakarta.persistence.Tuple;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class GetUserByIdCommand extends Command {
    @Setter
    private String userId;
    @Getter
    private Map<String, Object> userInfo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Override
    public void execute() {
        try{
            Tuple rawUser = userRepository.getUser(UUID.fromString(userId));
            if(rawUser == null){
                this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
            }
            else{
                Map<String, Object> tupleMap = new HashMap<>();
                rawUser.getElements().forEach(element -> tupleMap.put(element.getAlias(), rawUser.get(element)));
                if(tupleMap.get("image_id") != null)
                    tupleMap.put("image_url", firebaseService.getPhotoUrl(String.valueOf(tupleMap.get("image_id"))));
                userInfo = tupleMap;
                this.setState(new ResponseStatus(ResponseState.Success, "User retrieved successfully"));
            }

        }catch (Exception e){
            ResponseFormulator.formulateException(this,e);
        }
        ResponseFormulator.formulateLogger(log,this.getState());

    }
}
