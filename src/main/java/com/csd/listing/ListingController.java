package com.csd.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ListingController {
    @Autowired
    private ListingRepository listingRepository;
    void ListingRepository(ListingRepository repository) {
        this.listingRepository = repository;
    }

    @GetMapping("/listingpage")
    public Iterable<Listing> getListings() {
        return listingRepository.findAll();
    }

    @PostMapping("/listingpage/createlisting")
    public Listing addListing(@RequestBody Listing listing) {
        return listingRepository.save(listing);
    }

    @GetMapping("/listingpage/{id}")
    public Optional<Listing> findListingById(@PathVariable Long id) {
        return listingRepository.findListingById(id);
    }

}
