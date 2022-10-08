package com.csd.user;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//to permit cross origin communication
@CrossOrigin
@RestController
public class UserController {
    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public UserController(UserRepository repository, BCryptPasswordEncoder encoder){
        this.userRepository = repository;
        this.encoder = encoder;
    }

    // Login
//    @GetMapping("/login")

    // Sign up
    @PostMapping("/signup")
    public UserDTO addUser(@Valid @RequestBody User user) {
        // for BCrypt authorization
        user.setPassword(encoder.encode(user.getPassword()));
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
