package com.csd.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String contactNo;
    public UserDTO(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.contactNo = user.getContactNo();
    }

}
