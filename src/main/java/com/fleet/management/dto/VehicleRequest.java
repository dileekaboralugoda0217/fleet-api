package com.fleet.management.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fleet.management.model.VehicleStatus;
import com.fleet.management.model.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Data Transfer Object for creating and updating vehicle records.
 * Encapsulates client input with Jakarta Validation constraints.
 */
public class VehicleRequest {

    @NotBlank(message = "registrationNumber is required and cannot be blank")
    private String registrationNumber;

    @NotBlank(message = "make is required and cannot be blank")
    private String make;

    @NotBlank(message = "model is required and cannot be blank")
    private String model;

    @NotNull(message = "vehicleType is required (CAR, VAN, TRUCK, BUS)")
    private VehicleType vehicleType;

    @NotNull(message = "status is required (ACTIVE, MAINTENANCE, INACTIVE)")
    private VehicleStatus status;

    @NotNull(message = "odometerKm is required")
    @PositiveOrZero(message = "odometerKm must be greater than or equal to zero")
    private Double odometerKm;

    public VehicleRequest() {
    }

    public VehicleRequest(String registrationNumber, String make, String model,
                          VehicleType vehicleType, VehicleStatus status, Double odometerKm) {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.status = status;
        this.odometerKm = odometerKm;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @JsonIgnore
    public String getTrimmedRegistrationNumber() {
        return registrationNumber != null ? registrationNumber.trim() : null;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getMake() {
        return make;
    }

    @JsonIgnore
    public String getTrimmedMake() {
        return make != null ? make.trim() : null;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    @JsonIgnore
    public String getTrimmedModel() {
        return model != null ? model.trim() : null;
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
