package com.talk.social.post_service.dto;

import java.time.Instant;

public record PostResponseDTO(
    Long postId,         // Renamed for clarity
    String content,
    String mediaUrl,
//    String authorName,   // We can map this from authorId later
    int likesCount,
    Instant createdAt
) {}