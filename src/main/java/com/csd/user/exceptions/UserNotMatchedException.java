package com.csd.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403 Error
public class UserNotMatchedException extends RuntimeException{
    public UserNotMatchedException(Long id) {
        super("User does not match: " + id);
    }
}