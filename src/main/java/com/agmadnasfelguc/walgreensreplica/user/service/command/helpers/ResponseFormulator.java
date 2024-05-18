package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.UserRequests;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import org.slf4j.Logger;

import java.util.Map;

public class ResponseFormulator {
    public static void formulateResponse(Logger logger, ResponseStatus state, String replyTopic, byte[] correlationId, UserRequests userRequests, Map<String,Object> payload) {
        if (state.getStatus().equals(ResponseState.Success) || state.getStatus().equals(ResponseState.Pending)){
            logger.info(state.getMessage());
        } else if (state.getStatus().equals(ResponseState.Failure)) {
            logger.error(state.getMessage());
        }
        userRequests.publishResponse(replyTopic, correlationId, state, payload);
    }
}
