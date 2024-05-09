package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.VerifyMailCheckOTPCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class VerifyMailCheckOtpProcessor extends Processor {
    @Override
    public void process() {
        VerifyMailCheckOTPCommand typeCastedCommand = (VerifyMailCheckOTPCommand) getCommand();
        Map<String,String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setSessionId(paramsInfo.get(Keys.sessionId));
        typeCastedCommand.setOtp(messageInfo.get(Keys.otp));

    }
}
