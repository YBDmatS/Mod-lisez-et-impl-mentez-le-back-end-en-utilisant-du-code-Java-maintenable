package com.chatop.api.repository;

import com.chatop.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 * Extends JpaRepository to provide CRUD operations for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Check if a user exists by their email address.
     *
     * @param email The email address to check for existence
     * @return true if a user with the given email exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Find a user by their email address.
     *
     * @param email The email address of the user to find
     * @return the User entity associated with the given email, or null if no user is found
     */
    Optional<User> findByEmail(String email);
}
