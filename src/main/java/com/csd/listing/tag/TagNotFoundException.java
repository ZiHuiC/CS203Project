package com.csd.listing.tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String val ) {
        super("Could not find tag: " + val);
    }
}