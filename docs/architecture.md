# Architecture — AI Life Operations System (Germany Edition)

## Overview

AI Life Operations System is a distributed platform designed to help users manage documents, tasks, and decisions using AI.

The system follows a microservices-based architecture with a centralized API Gateway.

---

## High-Level Architecture

```text
Frontend (React)
        ↓
API Gateway (Spring Cloud Gateway)
        ↓
Backend Services
        ↓
Database / Messaging / AI Layer

```
---

## Phase 0

### 1. user-service
Responsible for:
* User Profile
* User related API
* Future Integration

### Database Integration

user-service is connected to PostgreSQL.

Current database table:

```text
users
```

### Package Structure

user-service follows a layered package structure:

```text
controller  → handles REST APIs
service     → contains business logic
repository  → handles database access
entity      → maps database tables
dto         → request/response models
exception   → custom exceptions

```
Request flow:

```text
HTTP Request → Controller → Service → Repository → Database
```
Reason:

* clear separation of concerns
* easier testing
* easier future extension
* production-friendly structure

Base Endpoints:
* /api/users/health
* /api/users/me

### Error Handling

user-service uses centralized exception handling with `@RestControllerAdvice`.

All API errors follow a consistent structure:

```json
{
  "timestamp": "2026-05-02T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with email: vineet@example.com",
  "path": "/api/users/me",
  "fieldErrors": []
}
```

### Request Validation

Incoming API requests are validated using Jakarta Bean Validation.

Example validations:

- required fields with `@NotBlank`
- valid email format with `@Email`
- length limits with `@Size`

Reason:

- invalid data is rejected at API boundary
- business logic receives clean input
- improves API reliability

### API Response Standard

user-service uses a standard success response wrapper.

Example:

```json
{
  "timestamp": "2026-05-02T10:30:00Z",
  "success": true,
  "message": "Current user fetched successfully",
  "data": {
    "id": "11111111-1111-1111-1111-111111111111",
    "name": "Vineet Pareek",
    "email": "vineet@example.com",
    "country": "Germany",
    "provider": "GOOGLE"
  }
}
```

### 2. api-gateway

Responsible for:

- Acting as the single entry point for frontend requests
- Routing user-related APIs to user-service
- Preparing the platform for future authentication and rate limiting

Current Route:

```text
/api/users/**  →  user-service

ports:
api-gateway: 8080
user-service: 8081
```


Current limitations:


## Current Limitations

- No authentication
- No database persistence
- No async processing
- Gateway currently uses static service URL

## Local Infrastructure

The platform uses Docker Compose for local development infrastructure.

Current local components:

### PostgreSQL

Used for:

- user data
- query history
- task records
- document metadata
- audit logs

### Kafka

Used for asynchronous processing.

Planned event flows:

```text
document uploaded → AI analysis requested → AI analysis completed → task created
```

### Kafka UI
Used for local visibility into Kafka topics and messages.

### pgAdmin
Used for local PostgreSQL inspection and debugging.