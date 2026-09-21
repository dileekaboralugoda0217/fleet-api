# Fleet Management REST API

A production-ready, object-oriented REST API for fleet vehicle management built with **Java 21**, **Spring Boot 3**, and **SQLite**.

---

## 1. Project Overview

The Fleet Management API provides fleet administrators with full CRUD capabilities over vehicle records. The system enforces strict business validation rules, case-insensitive unique registration numbers, safe whitespace trimming, robust centralized error handling, and persistent data storage across restarts using SQLite.

---

## 2. Architecture & OOP Principles

The project adheres to clean architectural layering and Object-Oriented Programming (OOP) principles:

```
com.fleet.management
├── controller       # HTTP REST Layer (delegates to services, handles HTTP statuses)
│   └── VehicleController.java
├── service          # Abstraction & Business Logic Layer
│   ├── VehicleService.java       (Interface - Abstraction)
│   └── impl
│       └── VehicleServiceImpl.java (Implementation - Business Rules & Validations)
├── repository       # Persistence Layer (Spring Data JPA)
│   └── VehicleRepository.java    (Interface - Abstracted Database Access)
├── model            # Encapsulated Domain Entities & Enums
│   ├── Vehicle.java
│   ├── VehicleType.java          (CAR, VAN, TRUCK, BUS)
│   └── VehicleStatus.java        (ACTIVE, MAINTENANCE, INACTIVE)
├── dto              # Data Transfer Objects
│   ├── VehicleRequest.java       (Input validation with Jakarta Validation)
│   ├── VehicleResponse.java      (API view model)
│   └── ErrorResponse.java        (Consistent error envelope)
└── exception        # Error Handling & REST Exceptions
    ├── ResourceNotFoundException.java   (Maps to 404 Not Found)
    ├── DuplicateResourceException.java  (Maps to 409 Conflict)
    └── GlobalExceptionHandler.java      (@RestControllerAdvice for uniform errors)
```

---

## 3. Database Schema & Types

Vehicle data is stored in a persistent SQLite database file (`fleet.db`).

| Field | Database Column | SQL Type | Sensible Constraints | Description |
|---|---|---|---|---|
| `id` | `id` | `VARCHAR(64)` | `PRIMARY KEY`, read-only | Prefixed unique identifier (e.g., `VEH-XXXXX`), immutable |
| `registrationNumber` | `registration_number` | `VARCHAR(32)` | `NOT NULL`, `UNIQUE` | Trimmed, case-insensitive uniqueness |
| `make` | `make` | `VARCHAR(64)` | `NOT NULL` | Vehicle make |
| `model` | `model` | `VARCHAR(64)` | `NOT NULL` | Vehicle model |
| `vehicleType` | `vehicle_type` | `VARCHAR(16)` | `NOT NULL` | Enum: `CAR`, `VAN`, `TRUCK`, `BUS` |
| `status` | `status` | `VARCHAR(16)` | `NOT NULL` | Enum: `ACTIVE`, `MAINTENANCE`, `INACTIVE` |
| `odometerKm` | `odometer_km` | `DOUBLE` | `NOT NULL`, `>= 0` | Vehicle distance in kilometers |

---

## 4. API Endpoints Reference

Base URL: `http://localhost:8080/api/vehicles`

| Method | Endpoint | Description | Success Code | Error Codes |
|---|---|---|---|---|
| `POST` | `/api/vehicles` | Create a new vehicle | `201 Created` | `400 Bad Request`, `409 Conflict` |
| `GET` | `/api/vehicles` | Retrieve all vehicles (empty list `[]` if none) | `200 OK` | - |
| `GET` | `/api/vehicles/{id}` | Retrieve a vehicle by its ID | `200 OK` | `404 Not Found` |
| `PUT` | `/api/vehicles/{id}` | Replace all editable fields of a vehicle | `200 OK` | `400 Bad Request`, `404 Not Found`, `409 Conflict` |
| `DELETE` | `/api/vehicles/{id}` | Permanently delete a vehicle | `204 No Content` | `404 Not Found` |

---

## 5. Consistent JSON Error Responses

All API errors return a uniform JSON format via `GlobalExceptionHandler`:

```json
{
  "timestamp": "2026-09-21T22:45:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request payload",
  "path": "/api/vehicles",
  "details": [
    "registrationNumber: registrationNumber is required and cannot be blank",
    "odometerKm: odometerKm must be greater than or equal to zero"
  ]
}
```

- **HTTP 400 Bad Request**: Missing or blank fields, negative odometer, malformed JSON, or invalid enum values.
- **HTTP 404 Not Found**: Vehicle ID does not exist (`ResourceNotFoundException`).
- **HTTP 409 Conflict**: Registration number already in use by another vehicle (`DuplicateResourceException`).

---

## 6. How to Run and Test

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

## 7. Example cURL Commands

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
*Response: HTTP 200 OK (or 404 Not Found)*

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
*Response: HTTP 200 OK (or 400 / 404 / 409)*

### 5. Delete a Vehicle (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/vehicles/<VEHICLE_ID>
```
*Response: HTTP 204 No Content (or 404 Not Found)*
