# api-gateway

## Responsibility

`api-gateway` is the single entry point for frontend and external clients.

Current responsibilities:

- Route API requests to backend services
- Hide internal service URLs from frontend
- Prepare for future authentication and rate limiting
- Provide platform-level entry point

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring Boot Actuator

---

## Local Port

```text
8080
```
---

## Current Routes
```text
/api/users/** → user-service
```
Default local route target:
```text
http://localhost:8081
```
Docker route target:
```text
http://user-service:8081
```
---
## Heath Check
```text
http://localhost:8080/actuator/health
```
---
## Configuration
The gateway uses environment-based service URL configuration.

Property:
```properties
lifeops.services.user-service-url=${USER_SERVICE_URL:http://localhost:8081}
```
Docker environment variable:
```properties
USER_SERVICE_URL=http://user-service:8081
```
---
## Run Locally
Run user-service first:
```bash
cd backend/user-service
mvnw.cmd spring-boot:run
```

Run gateway:
```bash
cd backend/api-gateway
mvnw.cmd spring-boot:run
```

Test through gateway:
```test
http://localhost:8080/api/users/me
```

---
## Docker
Run using Docker Compose from project root:
```bash
docker compose -f infra/local/docker-compose.yml up --build
```
---
## Important Design Notes
* Acts as centralized API entry point
* Uses static routing in Phase 0
* Will later handle OAuth2/JWT validation
* Will later support rate limiting and request filtering

