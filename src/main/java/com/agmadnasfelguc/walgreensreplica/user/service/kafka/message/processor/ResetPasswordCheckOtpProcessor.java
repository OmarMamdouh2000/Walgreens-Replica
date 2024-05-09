package com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor;

import com.agmadnasfelguc.walgreensreplica.user.service.command.UpdatePasswordResetCommand;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;

import java.util.Map;

public class ResetPasswordCheckOtpProcessor extends Processor {

    @Override
    public void process() {
        UpdatePasswordResetCommand typeCastedCommand = (UpdatePasswordResetCommand) getCommand();
        Map<String,String> messageInfo = getMessageInfo().get(Keys.body);
        typeCastedCommand.setOtp(messageInfo.get(Keys.otp));
        typeCastedCommand.setPassword(messageInfo.get(Keys.email));
        typeCastedCommand.setPassword(messageInfo.get(Keys.password));
    }
}
