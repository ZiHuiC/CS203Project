package com.csd.user;

import com.csd.application.Application;
import com.csd.listing.Listing;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class User implements UserDetails{
    private static final long serialVersionUID = 1L;
//    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", columnDefinition = "BINARY(16)")
//    private UUID uuid;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username should not be empty")
    private String username;

    @NotNull(message = "Password should not be empty")
    private String password;

    @NotNull(message = "Contact number should not be empty")
    @Size(min = 8, max = 8, message = "Contact should be 8 characters")
    private String contactNo;

    private String email;

    @OneToMany(mappedBy = "lister", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Listing> listings;

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @NotNull(message = "Authorities should not be null")
    // We define two roles/authorities: ROLE_USER or ROLE_ADMIN
    private String authorities;

    public User(String username, String password, String authorities, String contactNo){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.contactNo = contactNo;
    }

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
}
