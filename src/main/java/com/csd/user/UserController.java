package com.csd.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserDTO addUser(@Valid @RequestBody User user) {
        return new UserDTO(UserRepository.save(user));
    }

    @GetMapping("/profiles")
    public List<UserDTO> getUsers() {
        return UserRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }



}
