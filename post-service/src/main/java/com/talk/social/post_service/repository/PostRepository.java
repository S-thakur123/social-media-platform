package com.talk.social.post_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.talk.social.post_service.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	@Modifying
	@Query("UPDATE Post p set p.likesCount = p.likesCount + 1 WHERE p.id = :postId")
	int incrementLikes(Long postId);
	
	@Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likeCount = p.likeCount - 1 WHERE p.id = :postId AND p.likeCount > 0")
    int decrementLikeCount(@Param("postId") Long postId);
	
	@Modifying
    @Query("UPDATE Post p SET p.authorUsername = :newUsername WHERE p.authorId = :authorId")
    void updateAuthorUsername(@Param("authorId") Long authorId, @Param("newUsername") String newUsername);
}
