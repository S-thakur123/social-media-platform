package com.talk.social.post_service.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    public GlobalExceptionHandler(String string) {
		// TODO Auto-generated constructor stub
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleConflict(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        
        // We assume a data integrity violation in the Like table 
        // is due to our Unique Constraint (postId + username)
        errorResponse.put("error", "Action Conflict");
        errorResponse.put("message", "You have already liked this post!");
        errorResponse.put("status", "409");

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralError(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "Something went wrong. Please try again later.");
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}