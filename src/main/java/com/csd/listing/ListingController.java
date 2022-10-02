package com.csd.listing;

import com.csd.user.User;
import com.csd.user.UserNotFoundException;
import com.csd.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class ListingController {
    private ListingRepository listings;

    private UserRepository users;
    public ListingController(ListingRepository listings, UserRepository users) {
        this.listings = listings;
        this.users = users;
    }

    @GetMapping("/listingpage")
    public Iterable<Listing> getListings() {
        return listings.findAll();
    }

//     With lister nullable=false
    @PostMapping("/listingpage/createlisting")
    public Listing addListing(@RequestParam String username, @RequestBody Listing listing) {
        // need to add the user_id to the table
        return users.findByUsername(username).map(user -> {
            listing.setLister(user);
            return listings.save(listing);
        }).orElseThrow(() -> new UserNotFoundException(username));
    }

    @GetMapping("/listingpage/{id}")
    public Optional<Listing> findListingById(@PathVariable Long id) {
        return listings.findListingById(id);
    }

}
