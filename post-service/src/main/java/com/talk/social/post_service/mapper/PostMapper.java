package com.talk.social.post_service.mapper;

import org.springframework.stereotype.Component;

import com.talk.social.post_service.dto.PostResponseDTO;
import com.talk.social.post_service.model.Post;

@Component
public class PostMapper {

    public static PostResponseDTO toDto(Post post) {
    	if(post == null) return null;
        return new PostResponseDTO(
            post.getId(),
            post.getContent(),
            post.getMediaUrl(),
//            "Author #" + post.getAuthorId(), // Simplified for now
            post.getLikesCount(),
            post.getCreatedAt()
        );
    }
}
