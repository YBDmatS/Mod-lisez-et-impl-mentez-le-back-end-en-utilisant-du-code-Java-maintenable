package com.chatop.api.repository;

import com.chatop.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing User entities.
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
}
