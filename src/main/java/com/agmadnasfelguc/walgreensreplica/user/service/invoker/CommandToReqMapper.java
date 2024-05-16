package com.agmadnasfelguc.walgreensreplica.user.service.invoker;

import com.agmadnasfelguc.walgreensreplica.user.service.Utils.JsonReader;
import com.agmadnasfelguc.walgreensreplica.user.service.command.Command;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandToReqMapper {
    private static CommandToReqMapper commandToReqMapper = null;
    Logger logger = LoggerFactory.getLogger(CommandToReqMapper.class);
    @Getter
    private final HashMap<String, String> commandsMap = new HashMap<>();

    private CommandToReqMapper() {
        init();
    }

    private void init()  {
        try {

            Map jsonMap = JsonReader.readJsonFromResourcesAsMap("static/user_command_map.json");
            Reflections reflections = new Reflections("com.agmadnasfelguc.walgreensreplica.user.service.command");
            Set<Class<? extends Command>> commands = reflections.getSubTypesOf(Command.class);

            for (Class<? extends Command> commandClass : commands) {
                if (!Modifier.isAbstract(commandClass.getModifiers())) {
                    // get command class.getSimpleName()  and make the first letter lowercase and add it to the map
                    StringBuilder sb =new StringBuilder(commandClass.getSimpleName());
                    sb.setCharAt(0, Character.toLowerCase(commandClass.getSimpleName().charAt(0)));
                    String className = sb.toString();
                    System.out.println(className);
                    String requestName = (String) jsonMap.get(className);
                    if(requestName != null){
                        commandsMap.put(requestName, className);
                        logger.info("Command" + requestName + " added to command map");
                    }

                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());

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
