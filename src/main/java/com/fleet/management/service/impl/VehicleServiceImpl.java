package com.fleet.management.service.impl;

import com.fleet.management.dto.VehicleRequest;
import com.fleet.management.dto.VehicleResponse;
import com.fleet.management.model.Vehicle;
import com.fleet.management.repository.VehicleRepository;
import com.fleet.management.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of VehicleService providing direct CRUD operations.
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

        Vehicle vehicle = new Vehicle(
                UUID.randomUUID().toString(),
                trimmedReg != null ? trimmedReg : request.getRegistrationNumber(),
                request.getTrimmedMake() != null ? request.getTrimmedMake() : request.getMake(),
                request.getTrimmedModel() != null ? request.getTrimmedModel() : request.getModel(),
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
        return vehicleRepository.findById(id)
                .map(VehicleResponse::fromEntity)
                .orElse(null);
    }

    @Override
    public VehicleResponse updateVehicle(String id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id).orElse(null);
        if (vehicle == null) {
            return null;
        }

        String trimmedReg = request.getTrimmedRegistrationNumber();
        vehicle.update(
                trimmedReg != null ? trimmedReg : request.getRegistrationNumber(),
                request.getTrimmedMake() != null ? request.getTrimmedMake() : request.getMake(),
                request.getTrimmedModel() != null ? request.getTrimmedModel() : request.getModel(),
                request.getVehicleType(),
                request.getStatus(),
                request.getOdometerKm()
        );

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return VehicleResponse.fromEntity(updatedVehicle);
    }

    @Override
    public void deleteVehicle(String id) {
        vehicleRepository.deleteById(id);
    }
}
