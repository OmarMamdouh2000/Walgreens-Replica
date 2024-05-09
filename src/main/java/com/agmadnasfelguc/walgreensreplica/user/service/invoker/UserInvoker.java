package com.agmadnasfelguc.walgreensreplica.user.service.invoker;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.MessageCreator;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.creator.TemplatePaths;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.keys.Keys;
import com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor.Processor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserInvoker {
    private final CommandToReqMapper map;

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
            Class<?> processorClass = Class.forName("com.agmadnasfelguc.walgreensreplica.user.service.kafka.message.processor." + request + "Processor");
            Processor processor = (Processor) processorClass.getDeclaredConstructor().newInstance();
            Command command = (Command) commandClass.getDeclaredConstructor().newInstance();
            processor.init(command,body);
            processor.process();
            command.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserInvoker userInvoker = new UserInvoker();
        Map<String,String> bodyMap = new HashMap<>();
        bodyMap.put(Keys.email,"omarmmi2000@gmail.com");
        bodyMap.put(Keys.password,"changed");
        MessageCreator creator = new MessageCreator(TemplatePaths.userLoginPath,new HashMap<>(),bodyMap);
        JsonNode jsonNode = creator.createMessage();
        userInvoker.callCommand("UserLogin",jsonNode);
    }

}
