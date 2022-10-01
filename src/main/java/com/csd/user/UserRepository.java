package com.csd.user;

import java.util.Optional;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // define a derived query to find user by username
    Optional<User> findByUsername(String username);
}
