package com.csd.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

//to permit cross origin communication
@CrossOrigin(origins = "http://localhost:3000")

@RestController
public class UserController {
    private final UserRepository UserRepository;

    public UserController(UserRepository repository) {
        this.UserRepository = repository;
    }

    // Login
//    @GetMapping("/login")

    // Sign up
    @PostMapping("/signup")
    public User addUser(@Valid @RequestBody User user) {
        return UserRepository.save(user);
    }

    @GetMapping("/profiles")
    public Iterable<Object> getUsers() {
        var allUsers = UserRepository.findAll();
        var usernames = new ArrayList<>();
        for (User u : allUsers)
            usernames.add(u.getUsername());
        return usernames;
    }



}
