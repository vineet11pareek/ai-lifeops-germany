# user-service

## Responsibility

`user-service` manages user profile data for AI Life Operations System — Germany Edition.

Current responsibilities:

- Store user profile data
- Provide current user information
- Support future OAuth2/OIDC user mapping
- Provide user APIs for dashboard and personalization

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Boot Actuator
- springdoc-openapi

---

## Local Port

```text
8081
```
---
## Main APIs

```http
GET /api/users/health
GET /api/users/me
POST /api/users
```
---
## Swagger

http://localhost:8081/swagger-ui.html
---
## Health Check
http://localhost:8081/actuator/health
---

## Database
* Database: PostgresSQL
* Schema migration: Flyway
* Migration location: src/main/resources/db/migration
---
## Run Locally
Start infrastructure from project root:
```bash
docker compose -f infra/local/docker-compose.yml up -d postgres
```

Run service:
```bash
cd backend/user-service
mvnw.cmd spring-boot:run
```
---
## Run Tests
```bash
cd backend/user-service
mvnw.cmd test
```
---
## Docker
Run using full Docker Compose from project root:
```bash
docker compose -f infra/local/docker-compose.yml up --build
```
---
## Important Design Notes

* Uses layered package structure
* Uses Flyway for schema migrations
* Uses Hibernate ddl-auto=validate
* Uses Instant for timestamps
* Uses centralized exception handling
* Uses standard API response wrapper
* Supports X-Correlation-Id






