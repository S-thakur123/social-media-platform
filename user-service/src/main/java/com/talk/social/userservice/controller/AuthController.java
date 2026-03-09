package com.talk.social.userservice.controller;

import com.talk.social.userservice.dto.LoginRequest;
import com.talk.social.userservice.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public AuthController(AuthenticationManager authenticationManager,JwtUtil jwtUtil) {
    	this.authenticationManager = authenticationManager;
    	this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        // 1. Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        // 2. Prepare extra claims (e.g., status or roles)
        Map<String, Object> claims = new HashMap<>();
        claims.put("status", "VERIFIED"); // You can fetch this from the User entity

        // 3. Generate the token
        String token = jwtUtil.generateToken(authentication.getName(), claims);

        // 4. Return the token in a clean JSON response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        
        return ResponseEntity.ok(response);
    }
}