package com.csd.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//to permit cross origin communication
@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository repository) {
        this.userRepository = repository;
    }

    // Login
//    @GetMapping("/login")

    // Sign up
    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/signup")
    public UserDTO addUser(@Valid @RequestBody User user) {
        return new UserDTO(userRepository.save(user));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/profiles")
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }



}
