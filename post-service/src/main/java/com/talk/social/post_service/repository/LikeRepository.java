package com.talk.social.post_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.talk.social.post_service.model.Like;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    // Check if a specific user has already liked a specific post
    boolean existsByPostIdAndUserId(Long postId, String username);

    // Find the specific like record if you need to "Unlike" later
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);

    // Count likes for a specific post (if not using the denormalized counter)
    long countByPostId(Long postId);

    // Delete a like (Unlike functionality)
    void deleteByPostIdAndUserId(Long postId, Long userId);
}