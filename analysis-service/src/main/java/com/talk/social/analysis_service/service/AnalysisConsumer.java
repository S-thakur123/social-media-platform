package com.talk.social.analysis_service.service;

import java.util.function.Consumer;
import java.time.LocalDateTime;
//import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.talk.social.analysis_service.repository.UserActivityRepository;
import com.talk.social.analysis_service.model.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@Slf4j
public class AnalysisConsumer {

    @Autowired
    private UserActivityRepository repository;

    private static final Logger log = LoggerFactory.getLogger(AnalysisConsumer.class);
    
    @Bean
    public Consumer<ChatMessageEvent> consumeChatEvents() {
        return event -> {
            log.info("Analyzing new message from user: {}", event.getSenderId());
            
            // Logic: Every time a user sends a message, increase their score
            UserActivity activity = repository.findByUserId(event.getSenderId())
                    .orElse(new UserActivity());
                    
                    activity.setUserId(event.getSenderId());
                    activity.setMessageCount(0L);
                    activity.setEngagementScore(0.0);
            
            repository.save(activity);
        };
    }
}
