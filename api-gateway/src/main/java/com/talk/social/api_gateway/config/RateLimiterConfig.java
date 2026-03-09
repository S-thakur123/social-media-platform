package com.talk.social.api_gateway.config;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class RateLimiterConfig {

    private static final int MAX_REQUESTS = 5;   // requests allowed
    private static final long WINDOW = 60000;    // time window (1 minute)

    private final Map<String, RequestData> requestCounts = new ConcurrentHashMap<>();

    @Bean
    public HandlerFilterFunction<ServerResponse, ServerResponse> rateLimiterFilter() {
        return (request, next) -> {

            String clientIp = request.servletRequest().getRemoteAddr();

            RequestData data = requestCounts.computeIfAbsent(clientIp,
                    ip -> new RequestData(0, Instant.now().toEpochMilli()));

            synchronized (data) {

                long now = Instant.now().toEpochMilli();

                if (now - data.timestamp > WINDOW) {
                    data.count = 0;
                    data.timestamp = now;
                }

                data.count++;

                if (data.count > MAX_REQUESTS) {
                    return ServerResponse.status(429)
                            .body("Too many requests. Try again later.");
                }
            }

            return next.handle(request);
        };
    }

    static class RequestData {
        int count;
        long timestamp;

        RequestData(int count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}