package com.talk.social.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
public class UserEvent {
    private Long userId;
    private String username;
    private String email;
    public UserEvent() {}
	public UserEvent(Long userId, String username, String email) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
	}
	public String getEmail() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
    
}