package com.csd.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    private UserRepository users;

    public UserServiceImpl(UserRepository users){
        this.users = users;
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
    public User updateUser(User user){
        Optional<User> sameUsername = users.findById(user.getId());
        if (sameUsername.isPresent())
            return users.save(user);
        else
            throw new UserNotFoundException(user.getId());
    }

    @Override
    public void deleteUser(Long id){
        users.deleteById(id);
    }
}
