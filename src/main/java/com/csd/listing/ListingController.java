package com.csd.listing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ListingController {
    @Autowired
    private ListingRepository listingRepository;
    void ListingRepository(ListingRepository repository) {
        this.listingRepository = repository;
    }

    @GetMapping("/list")
    public Iterable<Listing> getListings() {
        return listingRepository.findAll();
    }

    @PostMapping("/list")
    public Listing addListing(@RequestBody Listing listing) {
        return listingRepository.save(listing);
    }

    @GetMapping("/clear")
    public void clearListings() {
        listingRepository.deleteAll();
    }

    @GetMapping("/list/{id}")
    public Listing findListingById(@PathVariable Long id) {
        return listingRepository.findListingById(id);
    }

}
