package com.fleet.management;

import com.fleet.management.dto.VehicleRequest;
import com.fleet.management.dto.VehicleResponse;
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
import static org.mockito.ArgumentMatchers.any;
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

        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleResponse response = vehicleService.createVehicle(request);

        assertThat(response.getRegistrationNumber()).isEqualTo("XYZ-123");
        assertThat(response.getMake()).isEqualTo("Honda");
        assertThat(response.getModel()).isEqualTo("Civic");
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("getVehicleById should return vehicle if found")
    void getVehicleById_Found() {
        when(vehicleRepository.findById("v-100")).thenReturn(Optional.of(sampleVehicle));

        VehicleResponse response = vehicleService.getVehicleById("v-100");

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("v-100");
        assertThat(response.getRegistrationNumber()).isEqualTo("XYZ-123");
    }

    @Test
    @DisplayName("getVehicleById should return null if not found")
    void getVehicleById_NotFound() {
        when(vehicleRepository.findById("non-existent")).thenReturn(Optional.empty());

        VehicleResponse response = vehicleService.getVehicleById("non-existent");

        assertThat(response).isNull();
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
    @DisplayName("updateVehicle should update and save vehicle if found")
    void updateVehicle_Found() {
        when(vehicleRepository.findById("v-100")).thenReturn(Optional.of(sampleVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleRequest updateRequest = new VehicleRequest(
                "XYZ-999",
                "Toyota",
                "Camry",
                VehicleType.CAR,
                VehicleStatus.MAINTENANCE,
                15000.0
        );

        VehicleResponse response = vehicleService.updateVehicle("v-100", updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getRegistrationNumber()).isEqualTo("XYZ-999");
        assertThat(response.getMake()).isEqualTo("Toyota");
        assertThat(response.getModel()).isEqualTo("Camry");
        assertThat(response.getStatus()).isEqualTo(VehicleStatus.MAINTENANCE);
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("updateVehicle should return null if not found")
    void updateVehicle_NotFound() {
        when(vehicleRepository.findById("non-existent")).thenReturn(Optional.empty());

        VehicleRequest updateRequest = new VehicleRequest(
                "XYZ-999",
                "Toyota",
                "Camry",
                VehicleType.CAR,
                VehicleStatus.MAINTENANCE,
                15000.0
        );

        VehicleResponse response = vehicleService.updateVehicle("non-existent", updateRequest);

        assertThat(response).isNull();
    }

    @Test
    @DisplayName("deleteVehicle should invoke repository deleteById")
    void deleteVehicle_Success() {
        vehicleService.deleteVehicle("v-100");

        verify(vehicleRepository).deleteById("v-100");
    }
}
