package com.fleet.management.util;

import com.fleet.management.model.VehicleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleIdGeneratorTest {

    @Test
    @DisplayName("generate() should return ID with default prefix VEH-")
    void generate_DefaultPrefix() {
        String id = VehicleIdGenerator.generate();

        assertThat(id).isNotNull();
        assertThat(id).startsWith("VEH-");
        assertThat(id).hasSize(40); // "VEH-" (4) + UUID (36)
    }

    @Test
    @DisplayName("generate(String) should return ID with custom prefix")
    void generate_CustomPrefix() {
        String id = VehicleIdGenerator.generate("FLT");

        assertThat(id).isNotNull();
        assertThat(id).startsWith("FLT-");
    }

    @Test
    @DisplayName("generate(VehicleType) should return ID prefixed by vehicle type")
    void generate_VehicleTypePrefix() {
        String carId = VehicleIdGenerator.generate(VehicleType.CAR);
        String truckId = VehicleIdGenerator.generate(VehicleType.TRUCK);

        assertThat(carId).startsWith("CAR-");
        assertThat(truckId).startsWith("TRUCK-");
    }

    @Test
    @DisplayName("generate(String) should fallback to VEH- if prefix is null or blank")
    void generate_NullOrBlankPrefix_Fallback() {
        assertThat(VehicleIdGenerator.generate((String) null)).startsWith("VEH-");
        assertThat(VehicleIdGenerator.generate("   ")).startsWith("VEH-");
    }
}
