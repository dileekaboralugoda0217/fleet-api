package com.fleet.management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

/**
 * Vehicle entity representing a fleet asset in the database.
 * Demonstrates encapsulation by keeping fields private, protecting the identifier from modification,
 * and exposing controlled business methods for state changes.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 32)
    private String registrationNumber;

    @Column(name = "make", nullable = false, length = 64)
    private String make;

    @Column(name = "model", nullable = false, length = 64)
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 16)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private VehicleStatus status;

    @Column(name = "odometer_km", nullable = false)
    private Double odometerKm;

    /**
     * Default constructor required by JPA.
     */
    public Vehicle() {
    }

    /**
     * Parameterized constructor for domain entity instantiation.
     */
    public Vehicle(String id, String registrationNumber, String make, String model,
                   VehicleType vehicleType, VehicleStatus status, Double odometerKm) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.status = status;
        this.odometerKm = odometerKm;
    }

    @PrePersist
    protected void onCreate() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
    }

    /**
     * Encapsulated method to update all editable fields of the vehicle.
     */
    public void update(String registrationNumber, String make, String model,
                       VehicleType vehicleType, VehicleStatus status, Double odometerKm) {
        this.registrationNumber = registrationNumber;
        this.make = make;
        this.model = model;
        this.vehicleType = vehicleType;
        this.status = status;
        this.odometerKm = odometerKm;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public Double getOdometerKm() {
        return odometerKm;
    }

    // Setters for mutable domain fields

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public void setOdometerKm(Double odometerKm) {
        this.odometerKm = odometerKm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id='" + id + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType=" + vehicleType +
                ", status=" + status +
                ", odometerKm=" + odometerKm +
                '}';
    }
}
