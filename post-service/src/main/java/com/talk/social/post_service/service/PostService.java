package com.talk.social.post_service.service;

import org.springframework.stereotype.Service;

import com.talk.social.post_service.model.Like;
import com.talk.social.post_service.model.Post;
import com.talk.social.post_service.repository.LikeRepository;
import com.talk.social.post_service.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    
    public PostService(PostRepository postRepository, LikeRepository likeRepository) {
		super();
		this.postRepository = postRepository;
		this.likeRepository = likeRepository;
	}

	@Transactional
    public void likePost(Long postId, String username) {
        // 1. Prevent duplicate likes
        if (likeRepository.existsByPostIdAndUserId(postId, username)) {
            throw new RuntimeException("Post already liked");
        }

        // 2. Record the like
        likeRepository.save(new Like(null, postId, username));

        // 3. Increment counter (Using a custom query for thread-safety)
        postRepository.incrementLikes(postId);
    }


    public Post createPost(Post post) {
        return postRepository.save(post);
    }
}