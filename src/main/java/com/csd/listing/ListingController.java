package com.csd.listing;

import com.csd.user.UserNotFoundException;
import com.csd.user.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
public class ListingController {
    private final ListingRepository listings;
    private final UserRepository users;

    public ListingController(ListingRepository listings, UserRepository users) {
        this.listings = listings;
        this.users = users;
    }
    @GetMapping("/listingpage")
    public List<ListingDTO> getListings() {
        return listings.findAll().stream().map(ListingDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/listingpage/createlisting")
    public ListingDTO addListing(@RequestParam Long userId, @RequestBody Listing listing) {
        // need to add the user_id to the table
        return users.findById(userId).map(user -> {
            listing.setLister(user);
            return new ListingDTO(listings.save(listing));
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @GetMapping("/listingpage/{id}")
    public ListingDTO findListingById(@PathVariable Long id) {
        if (listings.findListingById(id).isEmpty())
            throw new ListingNotFoundException(id);
        return new ListingDTO(listings.findListingById(id).get());
    }

}
