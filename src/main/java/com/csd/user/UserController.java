package com.csd.user;

import com.csd.listing.Listing;
import com.csd.listing.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserRepository UserRepository;

    void UserRepository(UserRepository repository) {
        this.UserRepository = repository;
    }

    // Login
//    @GetMapping("/Login")

    // Sign up
    @PostMapping("/SignUp")
    public User addUser(@RequestBody User user) {
        return UserRepository.save(user);
    }

    @GetMapping("/profiles")
    public Iterable<User> getUser() {
        return UserRepository.findAll();
    }
//
//
//    @GetMapping("/listingpage")
//    public Iterable<Listing> getListings() {
//        return listingRepository.findAll();
//    }
//
//    @PostMapping("/listingpage/createlisting")
//    public Listing addListing(@RequestBody Listing listing) {
//        return listingRepository.save(listing);
//    }
//
//    @GetMapping("/listingpage/{id}")
//    public Listing findListingById(@PathVariable Long id) {
//        return listingRepository.findListingById(id);
//    }


}
