package com.csd.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long Id) {
        super("Could not find user: " + Id);
    }

    // used when id is not known
    public UserNotFoundException(String username) {
        super("Could not find user: " + username);
    }
}
