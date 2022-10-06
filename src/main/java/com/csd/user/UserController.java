package com.csd.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserDTO addUser(@Valid @RequestBody User user) {
        return new UserDTO(userRepository.save(user));
    }

    @GetMapping("/profiles")
    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserDTO::new).collect(Collectors.toList());
    }



}
