package com.csd.user;

import javax.validation.Valid;

import com.csd.listing.Listing;
import com.csd.listing.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserRepository UserRepository;

    public UserController(UserRepository repository) {
        this.UserRepository = repository;
    }

    // Login
//    @GetMapping("/Login")

    // Sign up
    @PostMapping("/SignUp")
    public User addUser(@Valid @RequestBody User user) {
        return UserRepository.save(user);
    }

    @GetMapping("/profiles")
    public Iterable<User> getUsers() {
        return UserRepository.findAll();
    }



}
