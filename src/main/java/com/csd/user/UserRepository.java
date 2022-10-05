package com.csd.user;

import java.util.Optional;

// import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // define a derived query to find user by username
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
}
