package com.talk.social.post_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.talk.social.post_service.dto.PostResponseDTO;
import com.talk.social.post_service.mapper.PostMapper;
import com.talk.social.post_service.model.Post;
import com.talk.social.post_service.repository.PostRepository;
import com.talk.social.post_service.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository; // 1. Add this field

    // 2. Update constructor to include the repository
    public PostController(PostService postService, PostRepository postRepository) {
    	this.postService = postService;
    	this.postRepository = postRepository;
    }

	@PostMapping
    public Post create(@RequestBody Post post) {
        return postService.createPost(post);
    }
	
	@PostMapping("/{postId}/like")
    public ResponseEntity<String> like(@PathVariable Long postId, @RequestParam String username) {
        postService.likePost(postId, username);
        return ResponseEntity.ok("Post liked successfully");
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) { // Changed Post to PostResponseDTO
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
                
        return ResponseEntity.ok(PostMapper.toDto(post));
    }
}