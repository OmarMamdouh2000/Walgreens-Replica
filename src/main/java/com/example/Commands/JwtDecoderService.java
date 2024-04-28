package com.example.Commands;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
 public class JwtDecoderService {

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
