package com.talk.social.analysis_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.talk.social.analysis_service.model.UserActivity;

public interface UserActivityRepository extends MongoRepository<UserActivity, String>{
	Optional<UserActivity> findByUserId(String userId);
}
