package com.example.demo.cassandraJwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

	static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
    private static final String secret = "secretagmadnasflguc1234omarmamdouh1234base256key";
    // Set to store blacklisted tokens
    private static final Set<String> blacklistedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());

    //userId:role
    public static String generateToken(String userId, String role) {
        String hashedKey;
        if(role.equals("admin")){
            hashedKey = String.format("%s:%s",userId,role);
        }else{
            hashedKey = userId;
        }
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(hashedKey)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60 * 10)) // 10 hours validity
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public static String getUserIdFromToken(String token) {
        if (isTokenBlacklisted(token)) {
        	logger.error("Attempted access with a blacklisted token: {}", token);
            throw new SecurityException("Token is invalidated");
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Token parsed successfully: {}", token);
            return claims.getSubject();
        } catch (Exception e) {
        	logger.error("Invalid token: {}", token);
            return null;
        }
    }

    public static void blacklistToken(String token) {
        blacklistedTokens.add(token);
        logger.info("Token blacklisted successfully: {}", token);
    }

    public static boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}