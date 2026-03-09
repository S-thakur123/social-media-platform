package com.talk.social.notification_service.dto;

public record NotificationEvent(
		String recipientUsername,
	    String actorUsername,
	    Long postId,
	    String message) {}
