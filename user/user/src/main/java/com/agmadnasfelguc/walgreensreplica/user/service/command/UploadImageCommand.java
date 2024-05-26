package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.firebase.FirebaseService;
import com.agmadnasfelguc.walgreensreplica.user.model.User;
import com.agmadnasfelguc.walgreensreplica.user.repository.UserRepository;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.GetOrCreateUserSessionCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.command.helpers.ResponseFormulator;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UploadImageCommand extends Command{
    @Setter
    private String sessionId;

    @Setter
    private String imageId;

    @Autowired
    SessionCache sessionCache;

    @Autowired
    UserRepository userRepository;

    private String userId;

    @Autowired
    private GetOrCreateUserSessionCommand userSessionCommand;

    @Autowired
    private FirebaseService firebaseService;

    private String imageUrl;


    @Override
    public void execute() {
        try {
            String userId = JwtUtil.getUserIdFromToken(sessionId);
            if (userId == null) {
                this.setState(new ResponseStatus(ResponseState.Failure, "Invalid Session"));
            } else {

                setUpGetOrCreateUserSessionCommandAndExecute();
                Map<String, Object> sessionDetails = userSessionCommand.getUserMap();

                String oldImageId = String.valueOf(sessionDetails.get("imageId"));
                imageUrl = firebaseService.getPhotoUrl(imageId);
                sessionCache.updateSessionDetails(sessionId, "user", Map.of("imageId", this.imageId, "image_url", imageUrl));
                Optional<User> optionalUser = userRepository.findById(UUID.fromString(userId));
                User user = null;
                if (optionalUser.isPresent()) {
                    user = optionalUser.get();
                    user.setImageId(UUID.fromString(imageId));
                    userRepository.save(user);
                    if (!oldImageId.equals("null")) {
                        boolean deleted = firebaseService.deleteImage(oldImageId);
                        if (!deleted) {
                            this.setState(new ResponseStatus(ResponseState.Failure, "Failed to delete old image"));
                        }
                    }
                    this.setState(new ResponseStatus(ResponseState.Success, "Image saved successfully"));
                } else {
                    this.setState(new ResponseStatus(ResponseState.Failure, "User not found"));
                }
            }
        } catch (Exception e) {
            ResponseFormulator.formulateException(this, e);
        }
        ResponseFormulator.formulateResponse(log, this.getState(), this.getReplyTopic(), this.getCorrelationId(), this.getUserRequests(), Map.of("imageUrl", imageUrl));
    }

    private void setUpGetOrCreateUserSessionCommandAndExecute(){
        userSessionCommand.setSessionId(sessionId);
        userSessionCommand.setUserId(userId);
        userSessionCommand.execute();
    }
}
