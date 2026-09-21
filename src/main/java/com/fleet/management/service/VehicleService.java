package com.fleet.management.service;

import com.fleet.management.dto.VehicleRequest;
import com.fleet.management.dto.VehicleResponse;

import java.util.List;

/**
 * Service interface defining business operations for Vehicle management.
 * Provides abstraction between the API/Controller layer and underlying data persistence.
 */
public interface VehicleService {

    /**
     * Create a new vehicle record.
     *
     * @param request vehicle creation payload
     * @return created vehicle response
     */
    VehicleResponse createVehicle(VehicleRequest request);

    /**
     * Retrieve all vehicle records.
     *
     * @return list of vehicles (empty if none exist)
     */
    List<VehicleResponse> getAllVehicles();

    /**
     * Retrieve a vehicle by its unique identifier.
     *
     * @param id vehicle identifier
     * @return vehicle response
     */
    VehicleResponse getVehicleById(String id);

    /**
     * Replace all editable fields of an existing vehicle record.
     *
     * @param id vehicle identifier
     * @param request vehicle update payload
     * @return updated vehicle response
     */
    VehicleResponse updateVehicle(String id, VehicleRequest request);

    /**
     * Permanently delete a vehicle record by its identifier.
     *
     * @param id vehicle identifier
     */
    void deleteVehicle(String id);
}
