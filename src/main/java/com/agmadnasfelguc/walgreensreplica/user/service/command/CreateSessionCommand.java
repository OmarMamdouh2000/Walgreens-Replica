package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Data
public class CreateSessionCommand extends Command {
    private String email;
    private String userId;
    private String role;
    private String sessionId;
    private String firstName;
    private String lastName;

    @Autowired
    private SessionCache sessionCache;

    @Override
    public void execute() {
        this.sessionId = JwtUtil.generateToken(userId);
        sessionCache.createSession(sessionId,userId,role,email,firstName,lastName);

    }
}
