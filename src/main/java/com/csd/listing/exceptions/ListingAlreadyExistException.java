package com.csd.listing.exceptions;

public class ListingAlreadyExistException extends RuntimeException {

    public ListingAlreadyExistException(Long id) {
        super("Listing " + id + " already exists!");
    }
}