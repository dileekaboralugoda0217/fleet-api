package com.fleet.management.service.impl;

import com.fleet.management.dto.VehicleRequest;
import com.fleet.management.dto.VehicleResponse;
import com.fleet.management.exception.DuplicateResourceException;
import com.fleet.management.exception.ResourceNotFoundException;
import com.fleet.management.model.Vehicle;
import com.fleet.management.repository.VehicleRepository;
import com.fleet.management.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of VehicleService encapsulating business rules,
 * data sanitation (whitespace trimming), and uniqueness validation.
 */
@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public VehicleResponse createVehicle(VehicleRequest request) {
        String trimmedReg = request.getTrimmedRegistrationNumber();

        if (vehicleRepository.existsByRegistrationNumberIgnoreCase(trimmedReg)) {
            throw new DuplicateResourceException(
                    "Vehicle with registration number '" + trimmedReg + "' already exists"
            );
        }

        Vehicle vehicle = new Vehicle(
                UUID.randomUUID().toString(),
                trimmedReg,
                request.getTrimmedMake(),
                request.getTrimmedModel(),
                request.getVehicleType(),
                request.getStatus(),
                request.getOdometerKm()
        );

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return VehicleResponse.fromEntity(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getAllVehicles() {
        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponse::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(String id) {
        Vehicle vehicle = findVehicleOrThrow(id);
        return VehicleResponse.fromEntity(vehicle);
    }

    @Override
    public VehicleResponse updateVehicle(String id, VehicleRequest request) {
        Vehicle vehicle = findVehicleOrThrow(id);
        String trimmedReg = request.getTrimmedRegistrationNumber();

        // Check if registration number is already used by another vehicle
        if (vehicleRepository.existsByRegistrationNumberIgnoreCaseAndIdNot(trimmedReg, id)) {
            throw new DuplicateResourceException(
                    "Vehicle with registration number '" + trimmedReg + "' already exists"
            );
        }

        // Replace all editable fields
        vehicle.update(
                trimmedReg,
                request.getTrimmedMake(),
                request.getTrimmedModel(),
                request.getVehicleType(),
                request.getStatus(),
                request.getOdometerKm()
        );

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return VehicleResponse.fromEntity(updatedVehicle);
    }

    @Override
    public void deleteVehicle(String id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle with ID '" + id + "' not found");
        }
        vehicleRepository.deleteById(id);
    }

    private Vehicle findVehicleOrThrow(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with ID '" + id + "' not found"));
    }
}
