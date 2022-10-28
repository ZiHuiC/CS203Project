package com.csd.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(Long Id) {
        super("Could not find application " + Id);
    }
}
