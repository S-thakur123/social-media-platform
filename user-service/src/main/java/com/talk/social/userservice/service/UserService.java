package com.talk.social.userservice.service;

import com.talk.social.userservice.dto.UserEvent;
import com.talk.social.userservice.model.User;
import com.talk.social.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final StreamBridge streamBridge;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, StreamBridge streamBridge) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.streamBridge = streamBridge;
	}

	public User registerUser(User user) {
		// 1. Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// 2. Save to DB
		User savedUser = userRepository.save(user);

		// 3. Map to DTO and Publish to RabbitMQ
		UserEvent event = new UserEvent(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
		streamBridge.send("user-out-0", event);

		return savedUser;
	}
}