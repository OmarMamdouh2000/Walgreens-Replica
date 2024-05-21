package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.TwoFactorAuthLoginCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class TwoFactorAuthLoginProcessor extends Processor {

    @Override
    public void process() {
        TwoFactorAuthLoginCommand typeCastedCommand = (TwoFactorAuthLoginCommand) getCommand();


        Map<String, String> paramsInfo = getMessageInfo().get(Keys.params);
        Map<String, String> messageInfo = getMessageInfo().get(Keys.body);

        typeCastedCommand.setUserId(paramsInfo.get(Keys.userId));
        typeCastedCommand.setOtp(messageInfo.get(Keys.otp));
        typeCastedCommand.setEmail(messageInfo.get(Keys.email));



    }
}
