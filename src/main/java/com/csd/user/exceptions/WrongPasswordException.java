package com.csd.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403 Error
public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String password) {
        super("Password given is wrong: " + password);
    }
}