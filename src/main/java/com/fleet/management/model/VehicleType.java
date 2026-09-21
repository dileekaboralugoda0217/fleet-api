package com.fleet.management.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Supported vehicle types in the fleet.
 */
public enum VehicleType {
    CAR,
    VAN,
    TRUCK,
    BUS;

    @JsonCreator
    public static VehicleType fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle type cannot be empty");
        }
        try {
            return VehicleType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid vehicle type: '" + value + "'. Allowed values: CAR, VAN, TRUCK, BUS");
        }
    }
}
