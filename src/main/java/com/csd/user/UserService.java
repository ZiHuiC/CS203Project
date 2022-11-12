package com.csd.user;

import java.util.List;

import com.csd.user.UserInputs.UserContactInput;
import com.csd.user.UserInputs.UserNameInput;
import com.csd.user.UserInputs.UserPasswordInput;
import com.csd.user.UserInputs.UserProfileInput;


public interface UserService {
    List<User> listUsers();
    User getUser(Long id);
    User getUser(String username);
    User addUser(User user);
    User updateUserContact(Long id, UserContactInput userDTO);
    User updateUserName(Long id, UserNameInput userDTO);
    User updateUserPassword(Long id, UserPasswordInput userDTO);
    User updateUserProfile(Long id, UserProfileInput userProfileDTO);
    void deleteUser(Long id);
    boolean isUserOrAdmin(Long id);
}
