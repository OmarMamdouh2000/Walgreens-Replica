package com.agmadnasfelguc.walgreensreplica.user.service.command;

import com.agmadnasfelguc.walgreensreplica.user.cache.SessionCache;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class LogoutCommand extends Command{
    private String sessionId;

    @Autowired
    private SessionCache sessionCache;
    @Override
    public void execute() {
        sessionCache.deleteSession(sessionId);
    }

}
