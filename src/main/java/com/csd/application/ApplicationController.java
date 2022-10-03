package com.csd.application;

import java.util.Optional;

import com.csd.listing.Listing;
import com.csd.listing.ListingRepository;
import com.csd.user.UserNotFoundException;
import com.csd.user.UserRepository;
import com.csd.listing.ListingNotFoundException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// need to change to correct port 5507 i think
@CrossOrigin(origins = "http://localhost:3000")

@RestController
public class ApplicationController {
    private ApplicationRepository applications;
    private ListingRepository listings;
    private UserRepository users;

    public ApplicationController(ApplicationRepository applications, ListingRepository listings, UserRepository users){
        this.applications = applications;
        this.listings = listings;
        this.users = users;
    }

    //not working
    @GetMapping("/application/{id}")
    public Optional<Application> findApplicationById(@PathVariable Long id) {
        return applications.findApplicationById(id);
    }

    // show all the applications of this user
    // need do some security feature so that only this user and admin can see them
    //not working
    @GetMapping("/user/applications") // need fixing???
    public Iterable<Application> getApplications(@RequestParam String username) {
        return users.findByUsername(username).map(user -> {
            return applications.findAllByApplicant(user);
        }).orElseThrow(() -> new UserNotFoundException(username));
    }

    @PostMapping("/listingpage/{listingid}/apply")
    public Application addApplication(@RequestParam String username, @PathVariable Long listingid, 
            @RequestBody Application application) {
        // 
        return users.findByUsername(username).map(user -> {
            application.setApplicant(user);
            listings.findById(listingid).map(listing -> {
                application.setListing(listing);
                return listing;
            }).orElseThrow(() -> new ListingNotFoundException(listingid));
            
            return applications.save(application);
        }).orElseThrow(() -> new UserNotFoundException(username));
    }


}
