package com.example.Commands;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Final.OrderRepo;



@Service
public class Invoker {
    private HashMap<String,String> commandMap;
    
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private JwtDecoderService jwtDecoderService;
    public Invoker() {
        commandMap = new HashMap<>();
        commandMap.put("GetOrdersCommand","com.example.Commands.GetOrdersCommand");
        commandMap.put("GetActiveOrdersCommand", "com.example.Commands.GetActiveOrdersCommand");


    }
    public Object executeCommand(String commandName,Map<String,Object> data) {
        System.out.println(commandMap);
        if (commandMap.containsKey(commandName)) {
            try{
                String className = commandMap.get(commandName);
            Class<?> class1 = Class.forName(className);
            Constructor<?> constructor = class1.getDeclaredConstructor(OrderRepo.class, JwtDecoderService.class); // replace with your parameter types
            Object instance = constructor.newInstance(orderRepo, jwtDecoderService); // replace with your actual parameters

            // If your class has a method you want to invoke, you can do so like this:
            String methodName = "execute"; // replace with your method name
            Method method = class1.getDeclaredMethod(methodName,  Map.class); // replace with your method parameters
            System.out.println(data);
            return method.invoke(instance, data);
            }catch(Exception e){
                e.printStackTrace();
            }
            
        } 
        return "Command not found";
    }

}
