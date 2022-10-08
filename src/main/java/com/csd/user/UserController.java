package com.csd.user;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//to permit cross origin communication
@CrossOrigin
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

    //get a specific user by username
    @GetMapping("/user/{username}")
    public UserDTO getUser(@PathVariable String username) {
        if (userRepository.findByUsername(username).isEmpty())
            throw new UserNotFoundException(new Long(0));
        return new UserDTO(userRepository.findByUsername(username).get());
    }



}
