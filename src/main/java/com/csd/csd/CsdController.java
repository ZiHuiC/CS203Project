package com.csd.csd;

import com.csd.csd.Listing.Listing;
import com.csd.csd.Listing.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CsdController {
    @GetMapping(value = "/index")
    public String index(){
        return "index";
    }

    @Autowired
    private ListingRepository listingRepository;

    @GetMapping("/list")
    public Iterable<Listing> getListings() {
        return listingRepository.findAll();
    }

    @PostMapping("/list")
    public Listing addListing(@Valid @RequestBody Listing listing) {
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

    // Remove this 大便
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity security) throws Exception
        {
            security.httpBasic().disable();
        }
    }
}
