# Local Setup Guide

This guide explains how to run the project on your local machine.

---

## 1. What you need before starting

Install these tools:

- Java 21
- Maven
- Git
- Docker
- Docker Compose
- Node.js (for frontend later)

---

## 2. Clone the project

```bash
git clone https://github.com/YOUR_USERNAME/ai-lifeops-germany.git
cd ai-lifeops-germany
````
---

## 3. Project Folder Structure

+ ai-lifeops-germany/
    + backend/        → all backend services (Spring Boot)
    + frontend/       → UI application (React later)
    + infra/          → Docker, Kafka, PostgreSQL setup
    + docs/           → documentation files
    + .github/        → CI/CD pipelines
    + README.md       → project overview

---
## 4. What working now

Currently working:

- user-service runs on port 8081
- api-gateway runs on port 8080
- Gateway routes `/api/users/**` to user-service

Next steps will include:

+ Creating first backend service (user-service)
+ Adding API Gateway
+ Adding Docker setup
+ Running services locally

---
## 5. How we will run the project (later)

## Running user-service with PostgreSQL

Start local infrastructure first:

```bash
docker compose -f infra/local/docker-compose.yml up -d
````

For now, run services manually:

Terminal 1:

```bash
cd backend/user-service
mvnw.cmd spring-boot:run

````

Terminal 2:

```bash
cd backend/api-gateway
mvnw.cmd spring-boot:run
````

Test through gateway:

http://localhost:8080/api/users/health
http://localhost:8080/api/users/me

````
docker compose up
````

---
## 6. Current status

Right now:

❌ No services are running

❌ No backend is created

❌ No frontend is created

We are still in foundation setup phase


---
## 7. Next steps


---

## Local Infrastructure

The local development environment uses Docker Compose.

Current local infrastructure:

- PostgreSQL
- pgAdmin
- Kafka
- Kafka UI

Start infrastructure:

```bash
docker compose -f infra/local/docker-compose.yml up -d
```

Stop infrastructure:

```bash
docker compose -f infra/local/docker-compose.yml down
```

View running containers:

```bash
docker ps
```
### PostgreSQL
PostgreSQL runs on:


```text
localhost:5432
```

Default database:

```text
lifeops_db
```

Default user:

```text
lifeops
```
### pgAdmin
pgAdmin runs on:

```text
http://localhost:5050
```

### Kafka UI

Kafka UI runs on:

```text
http://localhost:8085
```

### Kafka Topics
Kafka topic auto-creation is disabled.

Topics must be created explicitly.

Initial topics:

```text
ai.analysis.requested
ai.analysis.completed
```


## Testing Error Handling

user-service provides centralized error responses.

Example error response:

```json
{
  "timestamp": "2026-05-02T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with email: wrong@example.com",
  "path": "/api/users/me",
  "fieldErrors": []
}
```

## Testing User Creation API

Run user-service and test:

```http
POST http://localhost:8081/api/users
Content-Type: application/json
```
Example valid body:

```JSON
{
  "fullName": "Test User",
  "email": "test.user@example.com",
  "country": "Germany",
  "provider": "GOOGLE"
}
```
Example invalid body:
```JSON
{
  "fullName": "",
  "email": "wrong-email",
  "country": "Germany",
  "provider": "GOOGLE"
}
```
Invalid requests return validation errors in a standard format.

## Running Tests

Run user-service tests:

```bash
cd backend/user-service
mvnw.cmd test
```
Expected result:
```text
BUILD SUCCESS
```
## Continuous Integration

GitHub Actions runs the user-service CI pipeline automatically.

Current pipeline file:

```text
.github/workflows/user-service-ci.yml
```
The pipeline runs:
```text
./mvnw test
./mvnw clean package -DskipTests
```
This ensures user-service builds successfully before future deployment.

## Running Full System with Docker Compose

Backend services use multi-stage Dockerfiles.

This means Docker builds the application JAR automatically.

From project root, start all services:
```bash
docker compose -f infra/local/docker-compose.yml up --build
```
Test through API Gateway:
```API
http://localhost:8080/api/users/health
http://localhost:8080/api/users/me
```

Current exposed ports:
```text
api-gateway     → 8080
user-service    → 8081
pgAdmin         → 5050
Kafka UI        → 8085
PostgreSQL      → 5432
Kafka           → 9092
```
Stop services
```bash
docker compose -f infra/local/docker-compose.yml down
```
## Health Checks

Backend services expose health endpoints.

API Gateway:

```text
http://localhost:8080/actuator/health
```
User Service:

```text
http://localhost:8081/actuator/health
```
When running with Docker Compose, check container health:
```bash
docker ps
```
Expected:

```text
lifeops-user-service   healthy
lifeops-api-gateway    healthy
```

## Testing Correlation ID

Call user-service with a custom correlation ID:

```bash
curl -H "X-Correlation-Id: test-123" http://localhost:8081/api/users/me
```
Expected:

* response contains X-Correlation-Id: test-123
* logs contain correlationId=test-123

## Swagger / OpenAPI

user-service exposes Swagger UI.

Run user-service and open:

```text
http://localhost:8081/swagger-ui.html
```

Raw OpenAPI JSON:
```Api
http://localhost:8081/v3/api-docs
```
Use Swagger UI to test:
```Api
GET /api/users/me
POST /api/users
```
## Docker Compose Profiles

The local Docker Compose setup uses profiles for optional tools.

Core services start with:

```bash
docker compose -f infra/local/docker-compose.yml up --build
```
Core services:

* PostgreSQL
* Kafka
* user-service
* api-gateway

Optional tools start with:
```bash
docker compose -f infra/local/docker-compose.yml --profile tools up --build
```
Optional tools:

* pgAdmin
* Kafka UI

Reason:

* keeps normal local startup lighter
* tools are available when needed
* matches production-style separation between application and debugging tools


