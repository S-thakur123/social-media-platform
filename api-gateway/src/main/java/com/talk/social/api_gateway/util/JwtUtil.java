package com.talk.social.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        // Modern approach: SecretKey must be long enough for HMAC-SHA
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Modern replacement for setSigningKey
                .build()
                .parseSignedClaims(token)     // Modern replacement for parseClaimsJws
                .getPayload();                // Modern replacement for getBody
    }

    public String getUserStatus(String token) {
        return getAllClaimsFromToken(token).get("status", String.class);
    }
}