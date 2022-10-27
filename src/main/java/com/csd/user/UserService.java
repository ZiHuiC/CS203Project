package com.csd.user;

import java.util.List;

import com.csd.user.UserDTOs.UserContactDTO;
import com.csd.user.UserDTOs.UserNameDTO;
import com.csd.user.UserDTOs.UserPasswordDTO;


public interface UserService {
    List<User> listUsers();
    User getUser(Long id);
    User getUser(String username);
    User addUser(User user);
    User updateUserContact(Long id, UserContactDTO userDTO);
    User updateUserName(Long id, UserNameDTO userDTO);
    User updateUserPassword(Long id, UserPasswordDTO userDTO);
    void deleteUser(Long id);
}
