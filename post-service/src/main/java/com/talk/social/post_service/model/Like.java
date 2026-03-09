package com.talk.social.post_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"postId", "userId"})
})
@Data @NoArgsConstructor @AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private Long userId;
    private String username;
    
    public Like() {}
    
    public Like(Long id, String username) {
    	this.id = id;
    	this.username = username;
    }
    
	public Like(Long id, Long postId, String username) {
		super();
		this.id = id;
		this.postId = postId;
		this.username = username;
	}

	public Like(Long id, Long postId, Long userId) {
		super();
		this.id = id;
		this.postId = postId;
		this.userId = userId;
	}
    
    
}