package com.agmadnasfelguc.walgreensreplica.user.service.command.helpers;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.UserRequests;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseState;
import com.agmadnasfelguc.walgreensreplica.user.service.response.ResponseStatus;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class ResponseFormulator {
    public static void formulateResponse(Logger logger, ResponseStatus state, String replyTopic, byte[] correlationId, UserRequests userRequests, Map<String,Object> payload) {
        formulateLogger(logger, state);
        userRequests.publishResponse(replyTopic, correlationId, state, payload);
    }

    public static void formulateLogger(Logger logger, ResponseStatus state){
        if (state.getStatus().equals(ResponseState.Success) || state.getStatus().equals(ResponseState.Pending)){
            logger.info(state.getMessage());
        } else if (state.getStatus().equals(ResponseState.Failure)) {
            logger.error(state.getMessage());
        }
    }

    public static void formulateException(Command command, Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        command.setState(new ResponseStatus(ResponseState.Failure, "Error occurred: " + e.getMessage() + "\nStackTrace: " + stackTrace));
    }
}
