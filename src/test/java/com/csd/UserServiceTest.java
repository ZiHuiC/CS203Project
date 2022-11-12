package com.csd;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.csd.user.*;
import com.csd.user.UserInputs.*;
import com.csd.user.exceptions.UserNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository users;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void addUser_NewUsername_ReturnSavedUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        when(users.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(users.save(any(User.class))).thenReturn(user);

        User savedUser = userService.addUser(user);

        assertNotNull(savedUser);
        verify(users).findByUsername(user.getUsername());
        verify(users).save(user);
    }

    @Test
    void addUser_NewUsername_ReturnNull(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        
        User savedUser = userService.addUser(user);

        assertNull(savedUser);
        verify(users).findByUsername(user.getUsername());
    }

    @Test
    void getUserById_Exist_ReturnUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        
        User foundUser = userService.getUser(1L);

        assertNotNull(foundUser);
        verify(users).findById(1L);
    }

    @Test
    void getUserById_DontExist_ReturnNull(){
        when(users.findById(any(Long.class))).thenReturn(Optional.empty());
        
        User foundUser = userService.getUser(1L);

        assertNull(foundUser);
        verify(users).findById(1L);
    }

    @Test
    void getUserByUsername_Exist_ReturnUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        
        User foundUser = userService.getUser(user.getUsername());

        assertNotNull(foundUser);
        assertInstanceOf(User.class, foundUser);
        verify(users).findByUsername(user.getUsername());
    }

    @Test
    void getUserByUsername_DontExist_ReturnNull(){
        when(users.findByUsername(any(String.class))).thenReturn(Optional.empty());
        
        User foundUser = userService.getUser("not here");

        assertNull(foundUser);
        verify(users).findByUsername("not here");
    }

    @Test
    void listUsers_Filled_ReturnList(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        when(users.findAll()).thenReturn(userList);
        
        List<User> foundUsers = userService.listUsers();

        assertNotNull(foundUsers);
        verify(users).findAll();
    }

    @Test
    void listUsers_NotFilled_ReturnList(){
        List<User> userList = new ArrayList<User>();
        when(users.findAll()).thenReturn(userList);
        
        List<User> foundUsers = userService.listUsers();

        assertNotNull(foundUsers);
        verify(users).findAll();
    }

    @Test
    void updateUserContact_UserExist_ReturnUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        UserContactInput userDTO = new UserContactInput(
             "12345678");
        when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(users.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUserContact(1L, userDTO);
        
        assertNotNull(updatedUser);
        assertInstanceOf(User.class, updatedUser);
        verify(users).findById(1L);
    }

    @Test
    void updateUserContact_UserDontExist_ThrowUserNotFoundException(){
        UserContactInput userDTO = new UserContactInput(
             "12345678");
        when(users.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserNotFoundException.class, 
                () -> userService.updateUserContact(1L, userDTO));
        
        verify(users).findById(1L);
        assertTrue(exception.getMessage().contains("Could not find user:"));
    }

    @Test
    void updateUserName_UserExist_ReturnUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        UserNameInput userDTO = new UserNameInput(
             "new", "name");
        when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(users.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUserName(1L, userDTO);
        
        assertNotNull(updatedUser);
        assertInstanceOf(User.class, updatedUser);
        verify(users).findById(1L);
    }

    @Test
    void updateUserName_UserDontExist_ThrowUserNotFoundException(){
        UserNameInput userDTO = new UserNameInput(
             "new", "name");
        when(users.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserNotFoundException.class, 
                () -> userService.updateUserName(1L, userDTO));
        
        verify(users).findById(1L);
        assertTrue(exception.getMessage().contains("Could not find user:"));
    }

    @Test
    void updateUserPassword_UserExist_ReturnUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        UserPasswordInput userDTO = new UserPasswordInput(
        "another password");
        when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(users.save(any(User.class))).thenReturn(user);
        when(encoder.encode(any(String.class))).thenReturn("abc");

        User updatedUser = userService.updateUserPassword(1L, userDTO);
        
        assertNotNull(updatedUser);
        assertInstanceOf(User.class, updatedUser);
        verify(users).findById(1L);
    }

    @Test
    void updateUserPassword_UserDontExist_ThrowUserNotFoundException(){
        UserPasswordInput userDTO = new UserPasswordInput(
        "another password");
        when(users.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserNotFoundException.class, 
                () -> userService.updateUserPassword(1L, userDTO));
        
        verify(users).findById(1L);
        assertTrue(exception.getMessage().contains("Could not find user:"));
    }

    @Test
    void updateUserProfile_UserExist_ReturnUser(){
        User user = new User(
            "test", "password", "firstname",
            "lastname", "62353535");
        UserProfileInput userDTO = new UserProfileInput(
        "99125290", "new", "name");
        when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(users.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUserProfile(1L, userDTO);
        
        assertNotNull(updatedUser);
        assertInstanceOf(User.class, updatedUser);
        verify(users).findById(1L);
    }

    @Test
    void updateUserProfile_UserDontExist_ThrowUserNotFoundException(){
        UserProfileInput userDTO = new UserProfileInput(
        "99125290", "new", "name");
        when(users.findById(any(Long.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UserNotFoundException.class, 
                () -> userService.updateUserProfile(1L, userDTO));
        
        verify(users).findById(1L);
        assertTrue(exception.getMessage().contains("Could not find user:"));
    }

    @Test
    void deleteUser_Exist_(){
        doNothing().when(users).deleteById(anyLong());
        
        userService.deleteUser(1L);
        verify(users).deleteById(1L);
    }

    @Test
    void deleteUser_DontExist_ThrowIllegalArgumentException(){
        doThrow(new IllegalArgumentException()).when(users).deleteById(anyLong());;

        assertThrows(
                IllegalArgumentException.class, 
                () -> userService.deleteUser(1L));
        
        verify(users).deleteById(1L);
    }
}
