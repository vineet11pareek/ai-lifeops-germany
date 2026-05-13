# task-service

## Responsibility

`task-service` manages task proposals and user approval workflows for AI Life Operations System — Germany Edition.

Current responsibilities:

- expose task service API boundary
- provide health and Swagger endpoints
- prepare for task persistence
- prepare for Kafka-based task creation from document analysis events

Future responsibilities:

- consume `document.analyzed` events
- create task proposals
- store task lifecycle
- support approve/reject workflow
- prepare for future task execution agents

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Kafka
- Spring Boot Actuator
- springdoc-openapi

## Local Port

```text
8084
```
Main APIs
```text
GET /api/tasks/health
```
Swagger
```text
http://localhost:8084/swagger-ui.html
```

Health Check
```text
http://localhost:8084/actuator/health
```
Gateway Route
```text
/api/tasks/** → task-service
```