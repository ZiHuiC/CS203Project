package com.csd.listing;

import com.csd.user.UserNotFoundException;
import com.csd.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
public class ListingController {
    private final ListingRepository listings;
    private final UserRepository users;

    public ListingController(ListingRepository listings, UserRepository users) {
        this.listings = listings;
        this.users = users;
    }
    @GetMapping("/listingpage")
    public Iterable<Object> getListings() {
        var allListings = listings.findAll();
        var listingNames = new ArrayList<>();
        for (Listing l : allListings) {
            listingNames.add(l.getName());
        }
        return (Iterable<Object>) listingNames;
    }

    @PostMapping("/listingpage/createlisting")
    public Long addListing(@RequestParam Long userId, @RequestBody Listing listing) {
        // need to add the user_id to the table
        return users.findById(userId).map(user -> {
            listing.setLister(user);
            return listings.save(listing);
        }).orElseThrow(() -> new UserNotFoundException(userId)).getId();
    }

    @GetMapping("/listingpage/{id}")
    public String findListingById(@PathVariable Long id) {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);
        var listing = listings.findListingById(id).get();
        return listing.getName() + ",\n" + listing.getDes() + ",\n" + listing.getLister().getUsername();
    }

}
