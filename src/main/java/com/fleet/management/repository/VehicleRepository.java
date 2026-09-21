package com.fleet.management.repository;

import com.fleet.management.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Vehicle persistence operations.
 * Demonstrates abstraction by decoupling data access mechanisms from business services.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    /**
     * Find a vehicle by its registration number ignoring case.
     */
    Optional<Vehicle> findByRegistrationNumberIgnoreCase(String registrationNumber);

    /**
     * Check if a vehicle with the given registration number already exists (case-insensitive).
     */
    boolean existsByRegistrationNumberIgnoreCase(String registrationNumber);

    /**
     * Check if another vehicle (different ID) already uses the given registration number (case-insensitive).
     */
    @Query("SELECT COUNT(v) > 0 FROM Vehicle v WHERE LOWER(v.registrationNumber) = LOWER(:regNum) AND v.id <> :id")
    boolean existsByRegistrationNumberIgnoreCaseAndIdNot(@Param("regNum") String registrationNumber, @Param("id") String id);
}
