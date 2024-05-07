package com.agmadnasfelguc.walgreensreplica.user.service.invoker;

import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.reflections.Reflections;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandToReqMapper {
    private static CommandToReqMapper commandToReqMapper = null;
    @Getter
    private final HashMap<String, String> commandsMap = new HashMap<>();

    private CommandToReqMapper() {
        init();
    }

    private void init()  {
        try {


            ObjectMapper objectMapper = new ObjectMapper();
            InputStream inputStream = CommandToReqMapper.class.getClassLoader().getResourceAsStream("static/user_command_map.json");
            Map jsonMap;
            if (inputStream == null) {
                throw new IllegalArgumentException("file not found! user_command_map.json");
            } else {
                // Directly parse InputStream to Map
                 jsonMap = objectMapper.readValue(inputStream, Map.class);

            }

            Reflections reflections = new Reflections("com.agmadnasfelguc.walgreensreplica.user.service.command");
            Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);

            for (Class<? extends Command> commandClass : commands) {
                if (!Modifier.isAbstract(commandClass.getModifiers())) {
                    String className = commandClass.getSimpleName();
                    String requestName = (String) jsonMap.get(className);
                    if(requestName != null)
                        commandsMap.put(requestName, className);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static synchronized CommandToReqMapper getInstance()
    {
        if (commandToReqMapper == null)
            commandToReqMapper = new CommandToReqMapper();

        return commandToReqMapper;
    }

//    public static void main(String[] args) {
//        CommandToReqMapper commandToReqMapper = CommandToReqMapper.getInstance();
//        System.out.println(commandToReqMapper.getCommandsMap());
//    }




}
