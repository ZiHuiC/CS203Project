package com.csd.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

// to communiciate with frontend
@CrossOrigin(origins = "http://localhost:5173")

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
    public ApplicationDTO findApplicationById(@PathVariable Long id) {
        var application = applications.findApplicationById(id);
        if (application.isEmpty())
            throw new ApplicationNotFoundException(id);
        return new ApplicationDTO(application.get());
    }

    // show all the applications of this user
    // need do some security feature so that only this user and admin can see them
    @GetMapping("/user/applications")
    public List<ApplicationDTO> getApplications(@RequestParam Long userId) {
        var user = users.findById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException(userId);
        return user.get().getApplications().stream().map(ApplicationDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/listingpage/{listingid}/apply")
    public ApplicationDTO addApplication(@RequestParam Long userId, @PathVariable Long listingid,
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
        return new ApplicationDTO(applications.save(application));
    }


}
