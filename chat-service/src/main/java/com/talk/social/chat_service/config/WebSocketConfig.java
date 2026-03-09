package com.talk.social.chat_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 1. Destinations where the server sends messages TO the client
        // Clients will subscribe to paths starting with /topic (broadcast) or /queue (private)
        config.enableSimpleBroker("/topic", "/queue");

        // 2. Prefix for messages sent FROM client TO server (@MessageMapping)
        config.setApplicationDestinationPrefixes("/app");
        
        // 3. Prefix for private messages (User-specific)
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The URL where the frontend connects (e.g., ws://localhost:8080/ws-chat)
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000") // Change to your frontend URL
                .withSockJS(); // Fallback for browsers that don't support WebSockets
    }
}
