package com.fleet.management.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Operational statuses for fleet vehicles.
 */
public enum VehicleStatus {
    ACTIVE,
    MAINTENANCE,
    INACTIVE;

    @JsonCreator
    public static VehicleStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }
        try {
            return VehicleStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: '" + value + "'. Allowed values: ACTIVE, MAINTENANCE, INACTIVE");
        }
    }
}
