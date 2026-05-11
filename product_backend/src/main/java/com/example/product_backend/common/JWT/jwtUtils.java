package com.example.product_backend.common.JWT;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class jwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationMS;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String userId,String email,String role){
        return Jwts.builder().setSubject(userId).claim("email",email).claim("role",role).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expirationMS)).signWith(getSigningKey(),SignatureAlgorithm.HS256).compact();
    }

    public Claims validateAndExtract(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserId(String token) {
        return validateAndExtract(token).getSubject();
    }

    public String extractEmail(String token) {
        return validateAndExtract(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return validateAndExtract(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return validateAndExtract(token).getExpiration().before(new Date());
    }
}
