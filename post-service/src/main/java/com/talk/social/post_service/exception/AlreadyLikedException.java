package com.talk.social.post_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Automatically returns 409
public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException(String message) {
        super(message);
    }
}