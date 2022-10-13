package com.csd.user;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    User getUser(Long id);
    User getUser(String username);
    User addUser(User user);
    // for when we want to add feature of changing password
    // User updateUser();
    void deleteUser(Long id);
}
