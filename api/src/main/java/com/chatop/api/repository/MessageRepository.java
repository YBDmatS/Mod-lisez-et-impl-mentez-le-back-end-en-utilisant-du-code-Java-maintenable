package com.chatop.api.repository;

import com.chatop.api.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Message entities.
 * Extends JpaRepository to provide CRUD operations for Message entities.
 */
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
