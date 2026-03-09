package com.talk.social.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserEvent {
    private Long userId;
    private String username;
    private String email;
	public UserEvent(Long userId, String username, String email) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
	}
    
}