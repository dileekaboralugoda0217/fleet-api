package com.fleet.management.util;

import com.fleet.management.repository.VehicleRepository;
import org.springframework.stereotype.Component;

/**
 * Spring-managed component responsible for generating sequential, prefixed
 * identifiers for vehicle records.
 *
 * <p>Format: {@code VEH-XXXXXXX} where {@code XXXXXXX} is a 7-digit zero-padded
 * counter starting at {@code 0000001} and incrementing by one for each new vehicle.</p>
 *
 * <p>Example sequence: {@code VEH-0000001}, {@code VEH-0000002}, …, {@code VEH-9999999}</p>
 */
@Component
public class VehicleIdGenerator {

    public static final String PREFIX = "VEH";
    private static final String ID_FORMAT = PREFIX + "-%07d";

    private final VehicleRepository vehicleRepository;

    public VehicleIdGenerator(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Generate the next sequential vehicle ID.
     *
     * <p>Determines the current highest numeric suffix stored in the database,
     * increments it by one, and returns the formatted identifier.</p>
     *
     * @return next vehicle ID, e.g. {@code VEH-0000001}
     */
    public String generate() {
        int nextSequence = vehicleRepository.findMaxSequenceNumber()
                .map(max -> max + 1)
                .orElse(1);

        return String.format(ID_FORMAT, nextSequence);
    }
}
