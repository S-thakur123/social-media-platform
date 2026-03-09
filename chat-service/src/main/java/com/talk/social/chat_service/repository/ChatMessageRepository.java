package com.talk.social.chat_service.repository;


import com.talk.social.chat_service.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    // Find chat history between two specific users
    List<ChatMessage> findBySenderIdAndRecipientId(String senderId, String recipientId);
}