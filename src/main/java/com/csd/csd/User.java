package com.csd.csd;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private UUID uuid;
    @NotNull(message = "Username should not be empty")
    @NotBlank(message = "Username should not be blank") // do we need or frontend??
    private String username;
    @NotNull(message = "Password should not be empty")
    private String password;
    @NotNull(message = "Contact number should not be empty")
    @Size(min = 8, max = 8, message = "Contact should be 8 characters")
    private String contactNo;
    private String email;

    @NotNull(message = "Authorities should not be null")
    // We define two roles/authorities: ROLE_USER or ROLE_ADMIN
    private String authorities;

    public User(String username, String password, String authorities,
                String contactNo){
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.contactNo = contactNo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(authorities));
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
}
