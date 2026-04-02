package com.chatop.api.repository;

import com.chatop.api.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Rental entities.
 * Extends JpaRepository to provide CRUD operations for Rental entities.
 */
public interface RentalRepository extends JpaRepository<Rental, Long> {
}
