package com.talk.social.chat_service.controller;

import com.talk.social.chat_service.model.ChatMessage;
import com.talk.social.chat_service.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository repository;
    
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageRepository repository) {
		super();
		this.messagingTemplate = messagingTemplate;
		this.repository = repository;
	}

	@MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        // 1. Set the server-side timestamp
        chatMessage.setTimestamp(new Date());

        // 2. Save the message to MongoDB history
        ChatMessage savedMsg = repository.save(chatMessage);

        // 3. Send to specific user (Private Messaging)
        // Recipient must subscribe to: /user/{userId}/queue/messages
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), 
                "/queue/messages", 
                savedMsg
        );
    }
}
