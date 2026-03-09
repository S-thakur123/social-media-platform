package com.talk.social.analysis_service.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "user_activity")
@Data
@Builder
public class UserActivity {

    @Id
    private String id;

    private String userId;

    private long messageCount;

    private LocalDateTime lastActive;

    private Double engagementScore;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}

	public LocalDateTime getLastActive() {
		return lastActive;
	}

	public void setLastActive(LocalDateTime lastActive) {
		this.lastActive = lastActive;
	}

	public Double getEngagementScore() {
		return engagementScore;
	}

	public void setEngagementScore(Double engagementScore) {
		this.engagementScore = engagementScore;
	}
    
    
}
