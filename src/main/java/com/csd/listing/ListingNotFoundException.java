package com.csd.listing;

class ListingNotFoundException extends RuntimeException {

    ListingNotFoundException(Long id) {
        super("Could not find listing " + id);
    }
}