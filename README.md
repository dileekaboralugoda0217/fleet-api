# Fleet Management REST API

A clean and straightforward REST API for fleet vehicle management built with **Java 21**, **Spring Boot 3**, and **SQLite**.

---

## 1. Project Overview

The Fleet Management API provides fleet administrators with core CRUD capabilities over vehicle records. Vehicle records persist across application restarts using SQLite.

---

## 2. Architecture & Project Structure

The project follows a clean, simplified layering:

```
com.fleet.management
├── controller       # HTTP REST Layer (delegates to services, returns HTTP responses)
│   └── VehicleController.java
├── service          # Abstraction & CRUD Logic Layer
│   ├── VehicleService.java       (Interface - Abstraction)
│   └── impl
│       └── VehicleServiceImpl.java (Implementation - Direct CRUD Operations)
├── repository       # Persistence Layer (Spring Data JPA)
│   └── VehicleRepository.java    (Interface - Abstracted Database Access)
├── model            # Domain Entities & Enums
│   ├── Vehicle.java
│   ├── VehicleType.java          (CAR, VAN, TRUCK, BUS)
│   └── VehicleStatus.java        (ACTIVE, MAINTENANCE, INACTIVE)
└── dto              # Data Transfer Objects
    ├── VehicleRequest.java       (Payload for Create and Update)
    └── VehicleResponse.java      (API view model)
```

---

## 3. Database Schema & Types

Vehicle data is stored in a persistent SQLite database file (`fleet.db`).

| Field | Database Column | SQL Type | Description |
|---|---|---|---|
| `id` | `id` | `VARCHAR(36)` | Generated UUID string, primary key |
| `registrationNumber` | `registration_number` | `VARCHAR(32)` | Vehicle registration |
| `make` | `make` | `VARCHAR(64)` | Vehicle make |
| `model` | `model` | `VARCHAR(64)` | Vehicle model |
| `vehicleType` | `vehicle_type` | `VARCHAR(16)` | Enum: `CAR`, `VAN`, `TRUCK`, `BUS` |
| `status` | `status` | `VARCHAR(16)` | Enum: `ACTIVE`, `MAINTENANCE`, `INACTIVE` |
| `odometerKm` | `odometer_km` | `DOUBLE` | Vehicle distance in kilometers |

---

## 4. API Endpoints Reference

Base URL: `http://localhost:8080/api/vehicles`

| Method | Endpoint | Description | Success Code | Not Found Code |
|---|---|---|---|---|
| `POST` | `/api/vehicles` | Create a new vehicle | `201 Created` | - |
| `GET` | `/api/vehicles` | Retrieve all vehicles | `200 OK` | - |
| `GET` | `/api/vehicles/{id}` | Retrieve a vehicle by its ID | `200 OK` | `404 Not Found` |
| `PUT` | `/api/vehicles/{id}` | Update an existing vehicle | `200 OK` | `404 Not Found` |
| `DELETE` | `/api/vehicles/{id}` | Delete a vehicle | `204 No Content` | - |

---

## 5. How to Run and Test

### Prerequisites
- **Java 21** or later (`java -version`)
- **Maven 3.8+** (`mvn -version`)

### Running Locally
```bash
mvn spring-boot:run
```
The application will start on `http://localhost:8080` and automatically create/connect to `fleet.db`.

### Running Automated Tests
```bash
mvn clean test
```

### Packaging
```bash
mvn clean package -DskipTests
java -jar target/fleet-management-1.0.0.jar
```

---

## 6. Example cURL Commands

### 1. Create a Vehicle (POST)
```bash
curl -X POST http://localhost:8080/api/vehicles \
  -H "Content-Type: application/json" \
  -d '{
    "registrationNumber": "ABC-1234",
    "make": "Toyota",
    "model": "Corolla",
    "vehicleType": "CAR",
    "status": "ACTIVE",
    "odometerKm": 15200.5
  }'
```
*Response: HTTP 201 Created*

### 2. Retrieve All Vehicles (GET)
```bash
curl -X GET http://localhost:8080/api/vehicles
```
*Response: HTTP 200 OK with JSON array*

### 3. Retrieve Vehicle by ID (GET)
```bash
curl -X GET http://localhost:8080/api/vehicles/<VEHICLE_ID>
```
*Response: HTTP 200 OK or 404 Not Found*

### 4. Update a Vehicle (PUT)
```bash
curl -X PUT http://localhost:8080/api/vehicles/<VEHICLE_ID> \
  -H "Content-Type: application/json" \
  -d '{
    "registrationNumber": "ABC-1234",
    "make": "Toyota",
    "model": "Corolla Hybrid",
    "vehicleType": "CAR",
    "status": "MAINTENANCE",
    "odometerKm": 18500.0
  }'
```
*Response: HTTP 200 OK or 404 Not Found*

### 5. Delete a Vehicle (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/vehicles/<VEHICLE_ID>
```
*Response: HTTP 204 No Content*
