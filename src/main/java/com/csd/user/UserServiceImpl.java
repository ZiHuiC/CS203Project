package com.csd.user;

import java.util.List;
import java.util.Optional;

import com.csd.user.UserInputs.UserContactInput;
import com.csd.user.UserInputs.UserNameInput;
import com.csd.user.UserInputs.UserPasswordInput;
import com.csd.user.UserInputs.UserProfileInput;
import com.csd.user.exceptions.UserNotFoundException;
import com.csd.user.exceptions.UserNotMatchedException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository users;
    private final BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository users, BCryptPasswordEncoder encoder){
        this.users = users;
        this.encoder = encoder;
    }

    @Override
    public List<User> listUsers(){
        return users.findAll();
    }

    @Override
    public User getUser(Long id){
        return users.findById(id).orElse(null);
    }

    @Override
    public User getUser(String username){
        return users.findByUsername(username).orElse(null);
    }

    @Override
    public User addUser(User user){
        Optional<User> sameUsername = users.findByUsername(user.getUsername());
        if (sameUsername.isEmpty())
            return users.save(user);
        else
            return null;
    }

    @Override
    public User updateUserContact(Long id, UserContactInput userDTO){
        Optional<User> usersResult = users.findById(id);
        if (usersResult.isPresent()){
            if (!isUserOrAdmin(id))
                throw new UserNotMatchedException(id);
            User user = usersResult.get();
            user.setContactNo(userDTO.getContact());
            return users.save(user);
        }
        
        else
            throw new UserNotFoundException(id);
    }

    @Override
    public User updateUserName(Long id, UserNameInput userDTO){
        Optional<User> usersResult = users.findById(id);
        if (usersResult.isPresent()){
            if (!isUserOrAdmin(id))
                throw new UserNotMatchedException(id);
            User user = usersResult.get();
            user.setFirstname((userDTO.getFirstname()));
            user.setLastname((userDTO.getLastname()));
            return users.save(user);
        }
        
        else
            throw new UserNotFoundException(id);
    }

    @Override
    public User updateUserPassword(Long id, UserPasswordInput userDTO){
        Optional<User> usersResult = users.findById(id);
        if (usersResult.isPresent()){
            if (!isUserOrAdmin(id))
                throw new UserNotMatchedException(id);
            User user = usersResult.get();
            user.setPassword(encoder.encode(userDTO.getPassword()));
            return users.save(user);
        }
        
        else
            throw new UserNotFoundException(id);
    }

    @Override
    public User updateUserProfile(Long id, UserProfileInput userDTO) {
        Optional<User> usersResult = users.findById(id);
        if (usersResult.isPresent()) {
            if (!isUserOrAdmin(id))
                throw new UserNotMatchedException(id);
            User user = usersResult.get();
            user.setFirstname(userDTO.getFirstname());
            user.setLastname(userDTO.getLastname());
            user.setContactNo(userDTO.getContact());
            return users.save(user);
        } else
            throw new UserNotFoundException(id);
    }

    @Override
    public void deleteUser(Long id){
        users.deleteById(id);
    }

    @Override
    public boolean isUserOrAdmin(Long id) {
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();
        return (getUser(authName).getId() == id
                || authName.compareTo("admin@lendahand.com") == 0);
    }
}
