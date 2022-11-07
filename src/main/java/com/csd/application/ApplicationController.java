package com.csd.application;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.csd.application.exception.ApplicationNotFoundException;
import com.csd.listing.ListingRepository;
import com.csd.listing.exceptions.ListingNotFoundException;
import com.csd.user.UserRepository;
import com.csd.user.UserService;
import com.csd.user.exceptions.UserNotFoundException;
import com.csd.user.exceptions.UserNotMatchedException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    private final ApplicationRepository applications;
    private final ListingRepository listings;
    private final UserRepository users;
    private UserService userService;

    public ApplicationController(ApplicationRepository applications, ListingRepository listings, 
            UserRepository users, UserService userService){
        this.applications = applications;
        this.listings = listings;
        this.users = users;
        this.userService = userService;
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
    public List<ApplicationDTO> getApplications(@RequestParam Long userId, @RequestParam(required = false) Long listingId) {
        var user = users.findById(userId);
        if (user.isEmpty())
            throw new UserNotFoundException(userId);
        
        List<Application> retrivedApplications = user.get().getApplications();
        
        if (listingId != null){
            var listing = listings.findById(listingId);
            if (listing.isEmpty())
                throw new ListingNotFoundException(listingId);
            Iterator<Application> itr = retrivedApplications.iterator();
            while (itr.hasNext()){
                Application app = itr.next();
                if (app.getListing().getId() != listingId)
                    itr.remove();
            }
        }
        return retrivedApplications.stream().map(ApplicationDTO::new).collect(Collectors.toList());
    }

    @PostMapping("/listingpage/{listingid}/newapplication")
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

    @DeleteMapping("/listingpage/application/removal/{id}")
    public void deleteApplication(@PathVariable Long id){
        Optional<Application> searchedApp = applications.findApplicationById(id);
        if (searchedApp.isEmpty())
            throw new ApplicationNotFoundException(id);
        Application app = searchedApp.get();
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();

        if (userService.getUser(authName).getId() == app.getListing().getLister().getId()
                || authName.compareTo("admin@lendahand.com") == 0)
            applications.deleteById(id);
        else 
            throw new UserNotMatchedException(app.getListing().getLister().getId());
    }
}
