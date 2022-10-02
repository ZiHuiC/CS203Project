package com.csd.listing;

public class ListingAlreadyExistException extends RuntimeException {

    public ListingAlreadyExistException(Long id) {
        super("Listing " + id + " already exists!");
    }
}