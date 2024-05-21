package com.example.Kafka;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.Final.CartTable;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
public class CartFormulator {
    Logger logger = LoggerFactory.getLogger(CartFormulator.class);

    public CartFormulator(){
        
    }

    public Object getData(String message){
        ObjectMapper objectMapper = new ObjectMapper();
        message=message.replace("\\", "");
        message=message.substring(1, message.length()-1);
        JsonNode jsonNode = null;
        try{
            // Convert the message string into a JsonNode
            jsonNode = objectMapper.readTree(message);

            // Extract the "data" field and convert it into a CartTable object
            JsonNode dataNode = jsonNode.get("data");
            System.out.println("DATA NODE: " + dataNode);
            CartTable cart = objectMapper.treeToValue(dataNode, CartTable.class);

            return cart;
        }catch(Exception e){
            logger.info("INFO: {}", jsonNode.get("data"));
            return jsonNode.get("data");
        }
    }


}
