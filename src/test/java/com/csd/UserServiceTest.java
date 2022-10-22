// package com.csd;

// import static org.junit.jupiter.api.Assertions.assertInstanceOf;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import com.csd.user.*;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;

// import java.util.*;

// @ExtendWith(MockitoExtension.class)
// public class UserServiceTest {
    
//     @Mock
//     private UserRepository users;

//     @InjectMocks
//     private UserServiceImpl userService;

//     @Test
//     void addUser_NewUsername_ReturnSavedUser(){
//         User user = new User(
//             "test", "password", "firstname",
//             "lastname", "62353535");
//         when(users.findByUsername(any(String.class))).thenReturn(Optional.empty());
//         when(users.save(any(User.class))).thenReturn(user);

//         User savedUser = userService.addUser(user);

//         assertNotNull(savedUser);
//         verify(users).findByUsername(user.getUsername());
//         verify(users).save(user);
//     }

//     @Test
//     void addUser_NewUsername_ReturnNull(){
//         User user = new User(
//             "test", "password", "firstname",
//             "lastname", "62353535");
//         when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        
//         User savedUser = userService.addUser(user);

//         assertNull(savedUser);
//         verify(users).findByUsername(user.getUsername());
//     }

//     @Test
//     void getUserById_Exist_ReturnUser(){
//         User user = new User(
//             "test", "password", "firstname",
//             "lastname", "62353535");
//         when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        
//         User foundUser = userService.getUser(1L);

//         assertNotNull(foundUser);
//         verify(users).findById(1L);
//     }

//     @Test
//     void getUserById_DontExist_ReturnNull(){
//         when(users.findById(any(Long.class))).thenReturn(Optional.empty());
        
//         User foundUser = userService.getUser(1L);

//         assertNull(foundUser);
//         verify(users).findById(1L);
//     }

//     @Test
//     void getUserByUsername_Exist_ReturnUser(){
//         User user = new User(
//             "test", "password", "firstname",
//             "lastname", "62353535");
//         when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        
//         User foundUser = userService.getUser(user.getUsername());

//         assertNotNull(foundUser);
//         assertInstanceOf(User.class, foundUser);
//         verify(users).findByUsername(user.getUsername());
//     }

//     @Test
//     void getUserByUsername_DontExist_ReturnNull(){
//         User user = new User(
//             "test", "password", "firstname",
//             "lastname", "62353535");
//         when(users.findByUsername(any(String.class))).thenReturn(Optional.empty());
        
//         User foundUser = userService.getUser(user.getUsername());

//         assertNull(foundUser);
//         verify(users).findByUsername(user.getUsername());
//     }

//     @Test
//     void listUsers_Filled_ReturnList(){
//         User user = new User(
//             "test", "password", "firstname",
//             "lastname", "62353535");
//         List<User> userList = new ArrayList<User>();
//         userList.add(user);
//         when(users.findAll()).thenReturn(userList);
        
//         List<User> foundUsers = userService.listUsers();

//         assertNotNull(foundUsers);
//         verify(users).findAll();
//     }

//     @Test
//     void listUsers_NotFilled_ReturnList(){
//         List<User> userList = new ArrayList<User>();
//         when(users.findAll()).thenReturn(userList);
        
//         List<User> foundUsers = userService.listUsers();

//         assertNotNull(foundUsers);
//         verify(users).findAll();
//     }

//     // @Test
//     // void deleteUser_DontExist(){
        
//     // }
// }
