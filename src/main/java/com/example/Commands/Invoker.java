package com.example.Commands;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.Final.PromoRepo;
import com.example.Final.UserUsedPromo;
import com.example.Final.UserUsedPromoRepo;
import com.example.Kafka.KafkaProducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.Cache.SessionCache;
import com.example.Final.CartRepo;

@Service
public class Invoker {
    private HashMap<String,String> commandMap;
    
    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private PromoRepo promoRepo;
    @Autowired
    private UserUsedPromoRepo userUsedPromoRepo;
    @Autowired
    private JwtDecoderService jwtDecoderService;
    @Autowired
    private KafkaProducer kafkaProducer;
    @Autowired
    private SessionCache sessionCache;

    public Invoker() {
        commandMap = new HashMap<>();
        commandMap.put("UpdateItemCountCommand","com.example.Commands.UpdateItemCountCommand");
        commandMap.put("UpdateItemCountCommandCache","com.example.Commands.UpdateItemCountCommandCache");
        commandMap.put("AddToSavedForLater", "com.example.Commands.AddToSavedForLaterCommand");
        commandMap.put("AddToSavedForLaterCache", "com.example.Commands.AddToSavedForLaterCommandCache");
        commandMap.put("ReturnFromSavedForLater", "com.example.Commands.ReturnFromSavedForLaterCommand");
        commandMap.put("ReturnFromSavedForLaterCache", "com.example.Commands.ReturnFromSavedForLaterCommandCache");
        commandMap.put("GetUserCart", "com.example.Commands.GetUserCart");
        commandMap.put("RemoveItem", "com.example.Commands.RemoveItem");
        commandMap.put("ChangeOrderType", "com.example.Commands.ChangeOrderType");
        commandMap.put("ApplyPromo", "com.example.Commands.ApplyPromo");
        commandMap.put("AddItem", "com.example.Commands.AddItem");
        commandMap.put("AddComment", "com.example.Commands.AddComment");
        commandMap.put("ProceedToCheckOutCommand", "com.example.Commands.ProceedToCheckOutCommand");
        commandMap.put("ConfirmCheckoutCommand", "com.example.Commands.ConfirmCheckoutCommand");


    }

    public Object executeCommand(String commandName,Map<String,Object> data) {
//        System.out.println(commandMap);
        if (commandMap.containsKey(commandName)) {
            try{
                String className = commandMap.get(commandName);
                Class<?> class1 = Class.forName(className);
                Constructor<?> constructor = class1.getDeclaredConstructor(CartRepo.class, JwtDecoderService.class , PromoRepo.class, UserUsedPromoRepo.class,KafkaProducer.class,SessionCache.class); // replace with your parameter types
                Object instance = constructor.newInstance(cartRepo, jwtDecoderService, promoRepo, userUsedPromoRepo,kafkaProducer,sessionCache); // replace with your actual parameters

                // If your class has a method you want to invoke, you can do so like this:
                String methodName = "execute"; // replace with your method name
                Method method = class1.getDeclaredMethod(methodName,  Map.class); // replace with your method parameters
//                System.out.println(data);
                return method.invoke(instance, data);
            }catch(Exception e){
//                e.printStackTrace();
//                return new ResponseEntity<>(e.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                return e.getCause().getMessage();
            }
            
        } 
        return "Command not found";
    }

}
