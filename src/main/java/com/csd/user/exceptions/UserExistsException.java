package com.csd.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// throws error 500 in springboot
@ResponseStatus(HttpStatus.CONFLICT) // 409 Error
public class UserExistsException extends RuntimeException{
    // used when id is not known
    public UserExistsException(String username) {
        super("This user exists: " + username);
    }
}
