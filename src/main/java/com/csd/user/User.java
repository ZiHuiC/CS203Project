package com.csd.user;

import com.csd.application.Application;
import com.csd.listing.Listing;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    @Serial
    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", columnDefinition = "BINARY(16)")
//    private UUID uuid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username should not be empty")
    private String username;

    @NotBlank(message = "Password should not be empty")
    private String password;

    @NotBlank(message = "First name should not be empty")
    private String firstname;

    @NotBlank(message = "Last name should not be empty")
    private String lastname;

    @NotBlank(message = "Contact number should not be empty")
    @Size(min = 8, max = 8, message = "Contact should be 8 characters")
    private String contactNo;

    @JsonIgnore
    @OneToMany(mappedBy = "lister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Listing> listings;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    // We define two roles/authorities: ROLE_USER or ROLE_ADMIN
    // default is ROLE_USER
    private String authorities = "ROLE_USER";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(authorities));
    }

    //need to override the abstract methods of abstract class of UserDetails
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    // only used when creating admin
    public User(String username, String password, String firstname, String lastname, String contactNo, String authorities) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactNo = contactNo;
        this.authorities = authorities;
    }
    // for creating users, default authority is user
    public User(String username, String password, String firstname, String lastname, String contactNo) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contactNo = contactNo;
    }
}
