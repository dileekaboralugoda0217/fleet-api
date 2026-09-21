package com.fleet.management.dto;

import com.fleet.management.model.Vehicle;
import com.fleet.management.model.VehicleStatus;
import com.fleet.management.model.VehicleType;

/**
 * Data Transfer Object representing the vehicle returned to API consumers.
 * Decouples database schema details from the public REST API contract.
 */
public class VehicleResponse {

    private String id;
    private String registrationNumber;
    private String make;
    private String model;
    private VehicleType vehicleType;
    private VehicleStatus status;
    private Double odometerKm;

    public VehicleResponse() {
    }

    public VehicleResponse(String id, String registrationNumber, String make, String model,
                           VehicleType vehicleType, VehicleStatus status, Double odometerKm) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.status = status;
        this.odometerKm = odometerKm;
    }

    /**
     * Factory method mapping a domain Vehicle entity to its public API response representation.
     */
    public static VehicleResponse fromEntity(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getRegistrationNumber(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getVehicleType(),
                vehicle.getStatus(),
                vehicle.getOdometerKm()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public Double getOdometerKm() {
        return odometerKm;
    }

    public void setOdometerKm(Double odometerKm) {
        this.odometerKm = odometerKm;
    }
}
