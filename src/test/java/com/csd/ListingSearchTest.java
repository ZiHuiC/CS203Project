package com.csd;


import com.csd.listing.Listing;
import com.csd.listing.ListingRepository;
import com.csd.user.User;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;


public class ListingSearchTest {

    @Autowired
    private ListingRepository repository;
    private User creator;
    private Listing listingA;
    private Listing listingB;

    @Before
    public void init() {
        listingA = new Listing();
        listingA.setName("TITLE A");
        listingA.setDes("DESCRIPTION A");
        listingA.setLister(creator);
        repository.save(listingA);

        listingB = new Listing();
        listingA.setName("TITLE B");
        listingA.setDes("DESCRIPTION B");
        listingA.setLister(creator);
        repository.save(listingB);
    }
    @Test
    public void givenListingTag_ReturnListingWithSameTag() {

    }

}