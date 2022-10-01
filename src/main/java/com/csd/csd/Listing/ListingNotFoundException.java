package com.csd.csd.Listing;

class ListingNotFoundException extends RuntimeException {

    ListingNotFoundException(Long id) {
        super("Could not find listing " + id);
    }
}