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

### Testing Strategy

user-service currently includes automated tests for:

- service-layer business logic
- controller API contract
- request validation behavior

Current test types:

```text
Unit tests        → service logic with Mockito
Web layer tests   → controller APIs with MockMvc
```

Reason:

* protects business rules
* verifies API response structure
* prepares services for CI/CD execution

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

## CI/CD

The project uses GitHub Actions for continuous integration.

Current pipeline:

```text
User Service CI
```

Current checks:

checkout repository
* set up Java 21
* run automated tests
* build application package

The pipeline runs on:

* push to main
* pull requests to main
* changes inside backend/user-service/**

## Containerization

The platform uses Docker for service packaging and Docker Compose for local orchestration.

Current containerized services:

- api-gateway
- user-service
- PostgreSQL
- pgAdmin
- Kafka
- Kafka UI

Service communication inside Docker uses Docker network names.

Example:

```text
api-gateway → http://user-service:8081
user-service → jdbc:postgresql://postgres:5432/lifeops_db
```

Reason:

* services are portable
* local setup is closer to deployment architecture
* avoids machine-specific configuration

### Multi-Stage Docker Build

Backend services use multi-stage Docker builds.

Build stage:

```text
eclipse-temurin:21-jdk
```
Runtime stage:
```text
eclipse-temurin:21-jre
```

Reason:

* build tools stay out of final runtime image
* final image is cleaner
* Docker build becomes repeatable
* CI/CD can build images without manual Maven packaging

## Health Checks and Actuator

Backend services expose Spring Boot Actuator endpoints for operational visibility.

Current exposed endpoints:

```text
/actuator/health
/actuator/info
```

Current services with Actuator:

* user-service
* api-gateway

The user-service health endpoint also checks PostgreSQL connectivity.

Reason:
* supports Docker health checks
* prepares for Kubernetes readiness/liveness probes
* helps monitoring tools verify service availability
* improves production operability

## Request Correlation and Logging

user-service supports request correlation using the `X-Correlation-Id` header.

Behavior:

- if request contains `X-Correlation-Id`, the service reuses it
- if missing, the service generates a new UUID
- the correlation ID is returned in response headers
- logs include the correlation ID using MDC

Example:

```text
X-Correlation-Id: 4f8a1d5e-5c67-4f8a-944d-5ab2a56a7c21
```
Reason:

* trace requests across distributed services
* simplify debugging
* prepare for OpenTelemetry tracing later

## API Documentation

user-service exposes OpenAPI documentation using springdoc-openapi.

Available URLs:

```text
Swagger UI:
http://localhost:8081/swagger-ui.html

OpenAPI JSON:
http://localhost:8081/v3/api-docs
```

Current documented APIs:

* GET /api/users/health
* GET /api/users/me
* POST /api/users

Reason:

* improves API discoverability
* helps frontend and backend collaboration
* supports manual API testing
* creates a production-friendly API contract foundation

### Docker Compose Profiles

The local environment separates core runtime services from optional debugging tools using Docker Compose profiles.

Core runtime:

- PostgreSQL
- Kafka
- user-service
- api-gateway

Tools profile:

- pgAdmin
- Kafka UI

Reason:

- avoids running unnecessary tools by default
- keeps local environment cleaner
- supports debugging when required

---

## Phase 1 - Frontend

The frontend application is created using React, TypeScript, and Vite.

Current frontend responsibility:

- show public landing/login page
- prepare routing for dashboard
- prepare future OAuth login integration

Current route:

```text
/ → Landing page
```
Current login buttons:

- Google login placeholder
- Facebook login placeholder

Reason:

- creates first user-facing entry point
- separates frontend from backend services
- prepares for authenticated dashboard flow

### Dashboard Layout

The frontend now includes a dashboard route.

Current routes:

```text
/           → Landing/Login page
/dashboard  → Dashboard page
```
Current dashboard sections:

- profile summary
- recent queries
- pending tasks
- upcoming deadlines
- available AI tools

Current AI tools shown:

- Bureaucracy Assistant
- Truth Layer
- Task Approval Layer

Reason:

- provides a centralized user workspace
- prepares UI for authenticated user flow
- supports future modules without redesigning navigation

### Google Login

The frontend integrates Google login using Google Identity Services through the React OAuth package.

Current behavior:

- user clicks Google login
- Google returns an ID token
- frontend stores the token temporarily
- user is redirected to dashboard


### Frontend to API Gateway Integration

The frontend communicates with backend services through `api-gateway`.

Current flow:

```text
React frontend → api-gateway → user-service → PostgreSQL
```
Current frontend API base URL:
```text
VITE_API_BASE_URL=http://localhost:8080
```

Current dashboard behavior:

- loads current user profile from /api/users/me
- displays backend user data
- uses centralized API client

Reason:

- frontend does not call internal services directly
- prepares for centralized authentication at gateway
- supports future rate limiting and routing policies

Current limitation:

- backend token validation is not implemented yet
- api-gateway does not yet enforce authentication

Next step:

- validate Google ID token on backend/gateway
- create or update user profile in user-service

### Authenticated User Context

The `/api/users/me` endpoint now uses the Google ID token from the Authorization header.

Current flow:

```text
React frontend
  → Authorization: Bearer <google_id_token>
  → api-gateway
  → user-service
  → Google token verification
  → find or create user
  → return user profile
```
Current behavior:

- backend validates Google ID token
- backend extracts user email, name, and Google subject
- user-service creates user profile if it does not exist
- /api/users/me no longer uses hardcoded demo user

Reason:

- moves from mock identity to authenticated user context
- prevents trusting frontend-only state
- prepares for protected routes and user-specific data

### Frontend Protected Routes

The frontend now protects private routes using a `ProtectedRoute` component.

Current behavior:

- `/` is public
- `/dashboard` requires a stored Google ID token
- unauthenticated users are redirected to `/`
- logout clears the token and returns user to landing page

Reason:

- prevents unauthenticated UI access
- improves user experience
- prepares for future role-based frontend routing

Security note:

- Frontend route protection is not sufficient for real security.
- Backend and API Gateway token validation are still required.

### Frontend CI

The frontend application uses GitHub Actions for continuous integration.

Current checks:

- install dependencies using `npm ci`
- build React/TypeScript application using `npm run build`

The workflow runs on:

- push to `main`
- pull requests to `main`
- changes inside `frontend/lifeops-ui/**`

Reason:

- catches TypeScript and build errors early
- keeps frontend changes validated before deployment
- aligns frontend with backend CI practices

### Frontend Containerization

The frontend is containerized using a multi-stage Docker build.

Build stage:

```text
node:24-alpine
```

Runtime Stage:
```text
nginx:1.27-alpine
```

Current behavior:

- React app is built into static assets
- Nginx serves the production frontend 
- React Router fallback is handled by Nginx
- Docker Compose exposes frontend on port 5173

Reason:

- production-like static serving
- smaller runtime image
- consistent local and deployment behavior
- prepares frontend for cloud deployment

## Phase 1 Architecture

Phase 1 introduced the frontend and authenticated user flow.

Current request flow:

```text
React Frontend
  → Google Login
  → stores Google ID token
  → calls API Gateway with Authorization header
  → api-gateway routes to user-service
  → user-service verifies Google ID token
  → user-service loads or creates user profile
  → PostgreSQL stores user data
```
Current frontend routes:
```text
/           → Landing/Login page
/dashboard  → Protected Dashboard
```
Current security behavior:

- frontend protects /dashboard
- frontend sends Google ID token as Bearer token
- backend verifies Google ID token
- unauthenticated or invalid requests return 401 Unauthorized

Current limitation:

- API Gateway forwards token but does not validate it yet
- Facebook login is only placeholder
- role-based authorization is not implemented yet

---

## Phase 2 — AI Query Service

Phase 2 introduces the first AI capability of the platform.

The `ai-service` is responsible for:

- receiving AI query requests
- validating user input
- calling Spring AI
- returning AI-generated responses
- storing query history
- preparing for async Kafka-based AI processing

Initial request flow:

```text
React Frontend
  → api-gateway
  → ai-service
  → Spring AI
  → LLM Provider
```
Planned APIs:
```text
POST /api/ai/queries
GET  /api/ai/queries
GET  /api/ai/queries/{id}
```

Reason:

- separates AI workload from user management
- allows independent scaling
- keeps AI provider configuration isolated
- prepares for future document analysis and Truth Layer modules


### ai-service

`ai-service` is introduced as the dedicated service for AI capabilities.

Current responsibility:

- expose AI service boundary
- provide health endpoint
- prepare for Spring AI integration
- prepare for query history persistence

Current endpoint:

```text
GET /api/ai/health
```
Current port:
```text
ai-service: 8082
```

Gateway route:
```text
/api/ai/** → ai-service
```
Reason:

- separates AI workload from user-service
- allows independent scaling
- isolates AI provider configuration
- prepares for AI query, document analysis, and Truth Layer modules

### Spring AI Integration

`ai-service` integrates Spring AI with OpenAI as the first model provider.

Current endpoint:

```text
POST /api/ai/chat
```
Current flow:
```text
Frontend / API client
  → api-gateway
  → ai-service
  → Spring AI ChatClient
  → OpenAI model
  → ai-service response
```
Current behavior:

- validates question input
- sends system prompt + user prompt to model
- returns answer, provider, and model
- uses synchronous processing

Reason:

- creates first real AI capability
- isolates AI provider usage inside ai-service
- prepares for query history, async processing, and future RAG

### ai-service Error Handling and Correlation

`ai-service` now uses centralized exception handling and request correlation.

Current behavior:

- validation errors return `400 Bad Request`
- AI provider failures return `502 Bad Gateway`
- unexpected errors return safe `500 Internal Server Error`
- each response includes `correlationId`
- logs include `correlationId`

Example error response:

```json
{
  "timestamp": "2026-05-05T10:30:00Z",
  "correlationId": "ai-test-123",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/ai/chat",
  "fieldErrors": [
    {
      "field": "question",
      "message": "Question is required"
    }
  ]
}
```

Reason:

- consistent API errors across services
- safer production behavior
- easier debugging and incident investigation

### AI Query Persistence

`ai-service` stores AI query history in PostgreSQL.

Current table:

```text
ai_queries
```
Stored data:

- query ID
- user ID placeholder
- question
- answer
- status
- provider
- model
- error message
- created and updated timestamps

Current query statuses:
```text
CREATED
PROCESSING
COMPLETED
FAILED
```

Current endpoints:
```text
POST /api/ai/chat
GET  /api/ai/queries
```

Reason:

- users can view query history
- failed AI requests can be audited
- prepares for async Kafka-based processing
- supports future dashboard history and analytics