package com.example.Final;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@Service
class JwtDecoderService {

   public Claims decodeJwtToken(String token, String secretKey) {
       try {
           Jws<Claims> jws = Jwts.parserBuilder()
                   .setSigningKey(secretKey.getBytes())
                   .build()
                   .parseClaimsJws(token);
           return jws.getBody();
       } catch (Exception e) {
           // Handle exception (e.g., invalid token)
           e.printStackTrace();
           return null;
       }
   }
}
@Service
public class Services {
	@Autowired
    private JwtDecoderService jwtDecoderService;
	@Autowired
	private OrderRepo orderRepo;
	public List<OrderTable> getOrders(String token){
		 String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
		    Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
	        if (claims != null) {
	        	String userId=(String) claims.get("userId");
	        	
	        	return orderRepo.getOrders(UUID.fromString(userId));
	        	
	        	
	        }else {
	        	return new ArrayList<OrderTable>();
	        }
	}
	public List<OrderTable> getActiveOrders(String token){
		 String secretKey = "ziad1234aaaa&&&&&thisisasecretekeyaaa"; 
		    Claims claims = jwtDecoderService.decodeJwtToken(token, secretKey);
	        if (claims != null) {
	        	String userId=(String) claims.get("userId");
	        	
	        	return orderRepo.getActiveOrders(UUID.fromString(userId));
	        	
	        	
	        }else {
	        	return new ArrayList<OrderTable>();
	        }
	}
	public OrderTable getOrderByDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        
        // Step 2: Convert the LocalDate to java.sql.Date
        
        System.out.println(localDate+"++++++++++++++++++++++");
        return orderRepo.getOrderByDate(localDate);
	}

}
