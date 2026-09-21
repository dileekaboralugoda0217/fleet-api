package com.fleet.management;

import com.fleet.management.dto.VehicleRequest;
import com.fleet.management.dto.VehicleResponse;
import com.fleet.management.exception.DuplicateResourceException;
import com.fleet.management.exception.ResourceNotFoundException;
import com.fleet.management.model.Vehicle;
import com.fleet.management.model.VehicleStatus;
import com.fleet.management.model.VehicleType;
import com.fleet.management.repository.VehicleRepository;
import com.fleet.management.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle sampleVehicle;

    @BeforeEach
    void setUp() {
        sampleVehicle = new Vehicle(
                "v-100",
                "XYZ-123",
                "Honda",
                "Civic",
                VehicleType.CAR,
                VehicleStatus.ACTIVE,
                12000.0
        );
    }

    @Test
    @DisplayName("createVehicle should trim spaces and save vehicle")
    void createVehicle_ShouldTrimAndSave() {
        VehicleRequest request = new VehicleRequest(
                "  XYZ-123  ",
                "  Honda  ",
                "  Civic  ",
                VehicleType.CAR,
                VehicleStatus.ACTIVE,
                12000.0
        );

        when(vehicleRepository.existsByRegistrationNumberIgnoreCase("XYZ-123")).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleResponse response = vehicleService.createVehicle(request);

        assertThat(response.getId()).startsWith("VEH-");
        assertThat(response.getRegistrationNumber()).isEqualTo("XYZ-123");
        assertThat(response.getMake()).isEqualTo("Honda");
        assertThat(response.getModel()).isEqualTo("Civic");
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("createVehicle should throw DuplicateResourceException when registration number exists")
    void createVehicle_ShouldThrow_WhenDuplicate() {
        VehicleRequest request = new VehicleRequest(
                "XYZ-123",
                "Honda",
                "Civic",
                VehicleType.CAR,
                VehicleStatus.ACTIVE,
                12000.0
        );

        when(vehicleRepository.existsByRegistrationNumberIgnoreCase("XYZ-123")).thenReturn(true);

        assertThatThrownBy(() -> vehicleService.createVehicle(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("already exists");

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("getVehicleById should return vehicle if found")
    void getVehicleById_Found() {
        when(vehicleRepository.findById("v-100")).thenReturn(Optional.of(sampleVehicle));

        VehicleResponse response = vehicleService.getVehicleById("v-100");

        assertThat(response.getId()).isEqualTo("v-100");
        assertThat(response.getRegistrationNumber()).isEqualTo("XYZ-123");
    }

    @Test
    @DisplayName("getVehicleById should throw ResourceNotFoundException if not found")
    void getVehicleById_NotFound() {
        when(vehicleRepository.findById("non-existent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.getVehicleById("non-existent"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("getAllVehicles should return list of vehicle responses")
    void getAllVehicles_ReturnsList() {
        when(vehicleRepository.findAll()).thenReturn(List.of(sampleVehicle));

        List<VehicleResponse> vehicles = vehicleService.getAllVehicles();

        assertThat(vehicles).hasSize(1);
        assertThat(vehicles.get(0).getId()).isEqualTo("v-100");
    }

    @Test
    @DisplayName("deleteVehicle should throw ResourceNotFoundException if vehicle does not exist")
    void deleteVehicle_NotFound() {
        when(vehicleRepository.existsById("non-existent")).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.deleteVehicle("non-existent"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(vehicleRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteVehicle should delete vehicle if exists")
    void deleteVehicle_Success() {
        when(vehicleRepository.existsById("v-100")).thenReturn(true);

        vehicleService.deleteVehicle("v-100");

        verify(vehicleRepository).deleteById("v-100");
    }
}
