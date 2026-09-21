package com.fleet.management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleet.management.dto.VehicleRequest;
import com.fleet.management.model.VehicleStatus;
import com.fleet.management.model.VehicleType;
import com.fleet.management.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/vehicles - successfully create vehicle")
    void createVehicle_Success() throws Exception {
        VehicleRequest request = new VehicleRequest(
                "ABC-1234",
                "Toyota",
                "Corolla",
                VehicleType.CAR,
                VehicleStatus.ACTIVE,
                15000.50
        );

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.registrationNumber", is("ABC-1234")))
                .andExpect(jsonPath("$.make", is("Toyota")))
                .andExpect(jsonPath("$.model", is("Corolla")))
                .andExpect(jsonPath("$.vehicleType", is("CAR")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.odometerKm", is(15000.50)));
    }

    @Test
    @DisplayName("GET /api/vehicles - returns empty list when none exist")
    void getAllVehicles_EmptyList() throws Exception {
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

    @Test
    @DisplayName("GET /api/vehicles - returns all vehicles")
    void getAllVehicles_ReturnsVehicles() throws Exception {
        VehicleRequest v1 = new VehicleRequest("REG-1", "Make1", "Model1", VehicleType.CAR, VehicleStatus.ACTIVE, 100.0);
        VehicleRequest v2 = new VehicleRequest("REG-2", "Make2", "Model2", VehicleType.TRUCK, VehicleStatus.MAINTENANCE, 200.0);

        mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(v1)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(v2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/vehicles/{id} - returns vehicle by ID")
    void getVehicleById_Success() throws Exception {
        VehicleRequest request = new VehicleRequest("REG-SINGLE", "Volvo", "FH16", VehicleType.TRUCK, VehicleStatus.ACTIVE, 55000.0);
        String createResponse = mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(get("/api/vehicles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.registrationNumber", is("REG-SINGLE")))
                .andExpect(jsonPath("$.make", is("Volvo")))
                .andExpect(jsonPath("$.model", is("FH16")))
                .andExpect(jsonPath("$.vehicleType", is("TRUCK")))
                .andExpect(jsonPath("$.status", is("ACTIVE")))
                .andExpect(jsonPath("$.odometerKm", is(55000.0)));
    }

    @Test
    @DisplayName("GET /api/vehicles/{id} - returns 404 for unknown ID")
    void getVehicleById_NotFound() throws Exception {
        mockMvc.perform(get("/api/vehicles/{id}", "unknown-id-12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/vehicles/{id} - successfully updates vehicle")
    void updateVehicle_Success() throws Exception {
        VehicleRequest request = new VehicleRequest("ORIG-REG", "MakeA", "ModelA", VehicleType.CAR, VehicleStatus.ACTIVE, 1000.0);
        String createResponse = mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        VehicleRequest updateRequest = new VehicleRequest(
                "UPDATED-REG",
                "MakeB",
                "ModelB",
                VehicleType.VAN,
                VehicleStatus.MAINTENANCE,
                25000.0
        );

        mockMvc.perform(put("/api/vehicles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.registrationNumber", is("UPDATED-REG")))
                .andExpect(jsonPath("$.make", is("MakeB")))
                .andExpect(jsonPath("$.model", is("ModelB")))
                .andExpect(jsonPath("$.vehicleType", is("VAN")))
                .andExpect(jsonPath("$.status", is("MAINTENANCE")))
                .andExpect(jsonPath("$.odometerKm", is(25000.0)));
    }

    @Test
    @DisplayName("PUT /api/vehicles/{id} - returns 404 for unknown ID")
    void updateVehicle_NotFound() throws Exception {
        VehicleRequest updateRequest = new VehicleRequest("REG-TEST", "Make", "Model", VehicleType.CAR, VehicleStatus.ACTIVE, 500.0);

        mockMvc.perform(put("/api/vehicles/{id}", "non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/vehicles/{id} - deletes vehicle and returns 204 with no body")
    void deleteVehicle_Success() throws Exception {
        VehicleRequest request = new VehicleRequest("DEL-REG", "Make", "Model", VehicleType.CAR, VehicleStatus.ACTIVE, 1000.0);
        String createResponse = mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(createResponse).get("id").asText();

        mockMvc.perform(delete("/api/vehicles/{id}", id))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        // Subsequent GET should return 404 Not Found
        mockMvc.perform(get("/api/vehicles/{id}", id))
                .andExpect(status().isNotFound());
    }
}
