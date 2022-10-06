package com.csd.application;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(Long Id) {
        super("Could not find application " + Id);
    }
}
