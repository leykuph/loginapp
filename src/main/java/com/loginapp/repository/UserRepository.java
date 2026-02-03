package com.loginapp.repository;

import com.loginapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 *
 * JpaRepository gives us for free: save(), findAll(), findById(), deleteById(), etc.
 * We add findByUsername because we need it for login (Step 5).
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by username.
     * Spring Data JPA implements this automatically from the method name.
     * Returns Optional so we can check if the user exists.
     */
    Optional<User> findByUsername(String username);
}
