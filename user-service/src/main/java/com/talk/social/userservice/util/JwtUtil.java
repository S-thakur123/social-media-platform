package com.talk.social.userservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime; // 86400000 for 1 day

    private SecretKey getSigningKey() {
        // Generates a secure key from your secret string
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .header().add("typ", "JWT").and() // Set header type
                .claims(extraClaims)              // Add custom claims (like status: VERIFIED)
                .subject(username)                // Set the user identity
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())        // Sign with our secret key
                .compact();                       // Convert to final String
    }
}