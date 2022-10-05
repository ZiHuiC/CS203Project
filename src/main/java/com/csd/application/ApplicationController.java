package com.csd.application;

import java.util.Optional;

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
    private final ApplicationRepository applications;
    private final ListingRepository listings;
    private final UserRepository users;

    public ApplicationController(ApplicationRepository applications, ListingRepository listings, UserRepository users){
        this.applications = applications;
        this.listings = listings;
        this.users = users;
    }

    @GetMapping("/application/{id}")
    public Optional<Application> findApplicationById(@PathVariable Long id) {
        return applications.findApplicationById(id);
    }

    // show all the applications of this user
    // need do some security feature so that only this user and admin can see them
    @GetMapping("/user/applications")
    public Iterable<Application> getApplications(@RequestParam Long userId) {
        return users.findById(userId).map(
                applications::findAllByApplicant).orElseThrow(() ->
                new UserNotFoundException(userId));
    }

    @PostMapping("/listingpage/{listingid}/apply")
    public Long addApplication(@RequestParam Long userId, @PathVariable Long listingid,
            @RequestBody Application application) {
        var user = users.findById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException(userId);

        var listing = listings.findListingById(listingid);
        if (listing.isEmpty())
            throw new ListingNotFoundException(listingid);

        var listingVal = listing.get();
        application.setApplicant(user.get());
        application.setListing(listingVal);
        return applications.save(application).getId();
    }


}
