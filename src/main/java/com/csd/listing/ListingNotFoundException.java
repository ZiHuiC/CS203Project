package com.csd.listing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class ListingNotFoundException extends RuntimeException {

    public ListingNotFoundException(Long id) {
        super("Could not find listing " + id);
    }
}