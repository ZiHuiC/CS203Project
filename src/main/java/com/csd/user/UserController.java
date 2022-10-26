package com.csd.user;

import javax.validation.Valid;

import com.csd.user.UserDTOs.UserContactDTO;
import com.csd.user.UserDTOs.UserDTO;
import com.csd.user.UserDTOs.UserNameDTO;
import com.csd.user.UserDTOs.UserPasswordDTO;
import com.csd.user.exceptions.UserExistsException;
import com.csd.user.exceptions.UserNotFoundException;
import com.csd.user.exceptions.WrongPasswordException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/newuser")
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
    @GetMapping("/user")
    public UserDTO getUser(@RequestParam String username) {
        User extractedUser = userService.getUser(username);
        if (extractedUser == null)
            throw new UserNotFoundException(username);
        return new UserDTO(extractedUser);
    }

    /**
     * get a specific user by id
     * @param id
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
    @DeleteMapping("/user/removal/{id}")
    public void deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
        }catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

    /**
     * update a specific user detail by id
     * @param id
     * @param updatedUser
     * @return the updated UserDTO
     */
    @PutMapping("/user/reseting/name/{id}")
    public UserDTO updateUserName(@PathVariable Long id, @Valid @RequestBody UserNameDTO updatedUser) {
        User user = userService.getUser(id);
        if (user == null)
            throw new UserNotFoundException(id);
        
        user.setFirstname(updatedUser.getFirstname());
        user.setLastname(updatedUser.getLastname());
        return new UserDTO(userService.updateUser(user));
    }

    /**
     * update a specific user detail by id
     * @param id
     * @param updatedUser
     * @return the updated UserDTO
     */
    @PutMapping("/user/reseting/password/{id}")
    public UserDTO updateUserPassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO updatedUser) {
        User user = userService.getUser(id);
        if (user == null)
            throw new UserNotFoundException(id);
        // String encodedOldPassword = encoder.encode(updatedUser.getOldPassword());
        // if (user.getPassword().compareTo(encodedOldPassword) != 0)
        //     throw new WrongPasswordException(encodedOldPassword);
        user.setPassword(updatedUser.getPassword());
        return new UserDTO(userService.updateUser(user));
    }

    /**
     * update a specific user detail by id
     * @param id
     * @param updatedUser
     * @return the updated UserDTO
     */
    @PutMapping("/user/reseting/contact/{id}")
    public UserDTO updateUserContact(@PathVariable Long id, @Valid @RequestBody UserContactDTO updatedUser) {
        User user = userService.getUser(id);
        if (user == null)
            throw new UserNotFoundException(id);
        user.setContactNo(updatedUser.getContact());
        return new UserDTO(userService.updateUser(user));
    }
}
