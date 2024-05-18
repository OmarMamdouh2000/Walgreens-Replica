package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class UploadImageCommand extends Command{
    private String sessionId;

    private String imageId;

    @Autowired
    SessionCache sessionCache;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FirebaseService firebaseService;

    Logger logger = LoggerFactory.getLogger(UploadImageCommand.class);

    @Override
    public void execute() {
        Map<String,Object> sessionDetails = sessionCache.getSessionSection(sessionId,"user");
        if(sessionDetails.isEmpty()){
            this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
        }
        else{
            String userId = String.valueOf(sessionDetails.get("userId"));
            String oldImageId = String.valueOf(sessionDetails.get("imageId"));
            sessionCache.updateSessionDetails(sessionId,"user", Map.of("imageId",this.imageId));
            Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
            User user = null;
            if(optionalUser.isPresent()){
                user = optionalUser.get();
                user.setImageId(UUID.fromString(imageId));
                userRepository.save(user);
                if(!oldImageId.equals("null")){
                    boolean deleted = firebaseService.deleteImage(oldImageId);
                    if(!deleted){
                        this.setState(new ResponseStatus(ResponseState.Failure, "Failed to delete old image"));
                    }
                }
                this.setState(new ResponseStatus(ResponseState.Success, "Image saved successfully"));
            }
            else{
                this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
            }
        }
        ResponseFormulator.formulateResponse(logger, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("imageId",imageId));
    }

}
