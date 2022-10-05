package com.csd.user;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long Id) {
        super("Could not find user " + Id);
    }
}
