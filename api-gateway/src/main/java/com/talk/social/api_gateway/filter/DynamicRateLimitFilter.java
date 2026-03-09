package com.talk.social.api_gateway.filter;

import com.talk.social.api_gateway.util.JwtUtil;
import org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.Duration;

@Component
public class DynamicRateLimitFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final JwtUtil jwtUtil;

    // Inject JwtUtil via constructor
    public DynamicRateLimitFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String tempStatus = "GUEST"; // Initial value
        
        try {
            String authHeader = request.headers().header("Authorization")
                    .stream().findFirst().orElse("");
            
            if (authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                tempStatus = jwtUtil.getUserStatus(token);
            }
        } catch (Exception e) {
            tempStatus = "GUEST";
        }

        // ✅ FIX: Create a final variable for the lambda
        final String finalStatus = tempStatus;

        int capacity = "VERIFIED".equalsIgnoreCase(finalStatus) ? 20 : 5;
        Duration period = Duration.ofSeconds(1);

        // Now the lambda uses 'finalStatus', which is guaranteed not to change
        return Bucket4jFilterFunctions.rateLimit(capacity, period, req -> finalStatus)
                .filter(request, next);
    }
}
/*
import org.springframework.cloud.gateway.server.mvc.filter.Bucket4jFilterFunctions;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;

@Component
public class DynamicRateLimitFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    // Using a map to simulate different limits for GUEST vs VERIFIED
    // In WebMVC, the rateLimit filter is usually applied at the route level.
    // This custom filter determines which limits to apply based on headers.
    
    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String status = request.headers().header("X-User-Status")
                .stream().findFirst().orElse("GUEST");

        int capacity;
        Duration period;

        if ("VERIFIED".equalsIgnoreCase(status)) {
            capacity = 20; // 20 requests
            period = Duration.ofSeconds(1); // per second
        } else {
            capacity = 5;  // 5 requests
            period = Duration.ofSeconds(1); // per second
        }

        // Using Bucket4j functionality provided by Spring Cloud Gateway WebMVC
        // We call the rateLimit function programmatically here
        return Bucket4jFilterFunctions.rateLimit(capacity, period, req -> status)
                .filter(request, next);
    }
    
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> 
            secureUserKeyResolver(jwtUtil).resolve(exchange).flatMap(key -> {
                // Logic: If key starts with VERIFIED_, use verifiedLimiter
                RedisRateLimiter selectedLimiter = key.startsWith("VERIFIED") 
                        ? verifiedLimiter : guestLimiter;

                return selectedLimiter.isAllowed("post-service", key)
                    .flatMap(response -> {
                        if (response.isAllowed()) return chain.filter(exchange);
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    });
            });
    }
}
*/