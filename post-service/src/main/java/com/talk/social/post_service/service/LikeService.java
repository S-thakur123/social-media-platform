package com.talk.social.post_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.talk.social.post_service.exception.PostNotFoundException;
import com.talk.social.notification_service.dto.NotificationEvent;
import com.talk.social.post_service.exception.AlreadyLikedException;
import com.talk.social.post_service.model.Like;
import com.talk.social.post_service.model.Post;
import com.talk.social.post_service.repository.LikeRepository;
import com.talk.social.post_service.repository.PostRepository;
import com.talk.social.post_service.util.UserContext;

@Service
//@RequiredArgsConstructor // This handles the constructor injection for final fields
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final RabbitTemplate rabbitTemplate; 

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, RabbitTemplate rabbitTemplate) {
    	this.likeRepository = likeRepository;
    	this.postRepository = postRepository;
    	this.rabbitTemplate = rabbitTemplate;
    }
    @Transactional
    public void likePost(Long postId) {
        String username = UserContext.getUsername(); 

        // 1. Check if like already exists
        if (likeRepository.existsByPostIdAndUserId(postId, username)) {
            throw new AlreadyLikedException("User " + username + " already liked post " + postId);
        }

        // 2. Save the like record
        Like like = new Like(postId, username);
        likeRepository.save(like);

        // 3. Update the counter (Atomic)
        int updatedRows = postRepository.incrementLikes(postId);
        if (updatedRows == 0) {
            throw new PostNotFoundException("Post not found with ID: " + postId);
        }
    }
    @Transactional
    public void toggleLike(Long postId) {
        // ... existing like logic ...
        String username = UserContext.getUsername();
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException("Post not found"));
        String postOwnerUsername = post.getAuthorUsername();
        boolean newLikeCreated = false;
        
        if(likeRepository.existsByPostIdAndUserId(postId, username)) {
        	likeRepository.deleteByPostIdAndUserId(postId, postId);
        	postRepository.decrementLikeCount(postId);
        }
        else {
        	likeRepository.save(new Like(null,postId,username));
        	postRepository.incrementLikes(postId);
        	newLikeCreated = true;
        }
        
        if (newLikeCreated) {
            NotificationEvent event = new NotificationEvent(
                postOwnerUsername, // You'll fetch this from the Post entity
                UserContext.getUsername(),
                postId,
                "liked your post!"
            );
            rabbitTemplate.convertAndSend("notificationExchange", "notificationRoutingKey", event);
        }
    }
}