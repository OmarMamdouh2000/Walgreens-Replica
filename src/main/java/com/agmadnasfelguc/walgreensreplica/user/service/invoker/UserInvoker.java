package com.agmadnasfelguc.walgreensreplica.user.service.invoker;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.Processor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class UserInvoker {
    private CommandToReqMapper map;

    public UserInvoker() {
        this.map = CommandToReqMapper.getInstance();
    }

    public void callCommand(String request, JsonNode body){
        String commandName = map.getCommandsMap().get(request);
        if(commandName == null){
            System.out.println("Command not found");
            return;
        }
        try {
            Class<?> commandClass = Class.forName("com.agmadnasfelguc.walgreensreplica.user.service.command." + commandName);
            Class<?> processorClass = Class.forName("com.agmadnasfelguc.walgreensreplica.user.service.kafka.message." + request + "Processor");
            Processor processor = (Processor) processorClass.getDeclaredConstructor().newInstance();
            Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
            processor.init(command,body);
            command.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
