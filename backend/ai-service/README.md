# ai-service

## Responsibility

`ai-service` handles AI-related capabilities for AI Life Operations System — Germany Edition.

Current responsibilities:

- provide AI service API boundary
- expose health and Swagger endpoints
- prepare for Spring AI integration
- prepare for AI query history persistence

Future responsibilities:

- process AI queries
- store query history
- support document analysis
- support Truth Layer analysis
- publish/consume Kafka AI events

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Flyway
- Spring Boot Actuator
- springdoc-openapi

## Local Port

```text
8082
```

## Main API
```text
GET /api/ai/health
```

## Swagger
```text
http://localhost:8082/swagger-ui.html
```

## Health Check
```text
http://localhost:8082/actuator/health
```

