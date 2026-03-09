package com.talk.social.notification_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.talk.social.notification_service.dto.UserEvent;

import java.util.function.Consumer;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
	@Bean
    public Consumer<UserEvent> consumeUserCreated() {
        return event -> {
            System.out.println("Notification received for User: " + event.getUsername());
            System.out.println("Sending welcome email to: " + event.getEmail());
        };
    }

}
