package com.csd.user;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(Long id);
    User getUser(String username);
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(Long id);
}
