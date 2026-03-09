package com.talk.social.post_service.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "posts")
@Data @Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String mediaUrl;
    private Long authorId; // From User-Service
    private String authorUsername;
    
    private int likesCount = 0; // Denormalized for performance

    @Column(updatable = false)
    private Instant createdAt = Instant.now();

    public Post() {}
	public Post(Long id, String content, String mediaUrl, Long authorId, String authorUsername, int likesCount, Instant createdAt) {
		super();
		this.id = id;
		this.content = content;
		this.mediaUrl = mediaUrl;
		this.authorId = authorId;
		this.authorUsername = authorUsername;
		this.likesCount = likesCount;
		this.createdAt = createdAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	public String getAuthorUsername() {
		return authorUsername;
	}
	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}
	public int getLikesCount() {
		return likesCount;
	}
	public void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	
	
    
    
}
