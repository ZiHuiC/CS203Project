package com.csd.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

//to permit cross origin communication
@CrossOrigin(origins = "http://localhost:5173")

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository repository) {
        this.userRepository = repository;
    }

    // Login
//    @GetMapping("/login")

    // Sign up
    @PostMapping("/signup")
    public User addUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/profiles")
    public Iterable<Object> getUsers() {
        var allUsers = userRepository.findAll();
        var usernames = new ArrayList<>();
        for (User u : allUsers)
            usernames.add(u.getUsername());

        return usernames;
    }



}
