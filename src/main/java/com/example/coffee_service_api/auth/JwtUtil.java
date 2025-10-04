package com.example.coffee_service_api.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    private final String jwtSecret;
    private final long expirationSeconds;

    public JwtUtil(String jwtSecret, long expirationSeconds) {
        this.jwtSecret = jwtSecret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String subject, String username) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .claim("username", username)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationSeconds * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }
}
