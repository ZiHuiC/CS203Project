package com.csd.user;

import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

//to permit cross origin communication
@CrossOrigin
@RestController
public class UserController {
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    public UserController(UserService userService, BCryptPasswordEncoder encoder){
        this.userService = userService;
        this.encoder = encoder;
    }

    /**
     * Add a new user with POST request to "/signup"
     * @param user
     * @return the newly added user as UserDTO
     */
    @PostMapping("/signup")
    public UserDTO addUser(@Valid @RequestBody User user) {
        // for BCrypt authorization
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userService.addUser(user);
        if (savedUser == null)
            throw new UserExistsException(user.getUsername());
        return new UserDTO(savedUser);
    }

    /**
     * List all user in the database
     * @return list of all users as UserDTO
     */
    @GetMapping("/profiles")
    public List<UserDTO> getUsers() {
        return userService.listUsers().stream().map(UserDTO::new).collect(Collectors.toList());
    }

    /**
     * get a specific user by username
     * @param username
     * @return the specified UserDTO
     */
    @GetMapping("/user/{username}")
    public UserDTO getUser(@PathVariable String username) {
        User extractedUser = userService.getUser(username);
        if (extractedUser == null)
            throw new UserNotFoundException(username);
        return new UserDTO(extractedUser);
    }

    /**
     * get a specific user by id
     * @param username
     * @return the specified UserDTO
     */
    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        User extractedUser = userService.getUser(id);
        if (extractedUser == null)
            throw new UserNotFoundException(id);
        return new UserDTO(extractedUser);
    }

    /**
     * remove a specific user by id
     * @param id
     */
    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
        }catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }
}
