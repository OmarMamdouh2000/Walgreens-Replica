package com.example.Kafka;

import org.springframework.stereotype.Component;

import com.example.Final.CartTable;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CartFormulator {
    public CartFormulator(){
        
    }
    public Object getData(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        Object result=null;
        message=message.replace("\\", "");
        message=message.substring(1, message.length()-1);
        try{
            System.out.println(message);
            result=objectMapper.convertValue(message, CartTable.class);

            return result;
        }catch(Exception e){
            result=message;
            return result;
        }
    }


}
