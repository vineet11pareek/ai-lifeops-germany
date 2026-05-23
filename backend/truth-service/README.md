# truth-service

## Responsibility

`truth-service` manages Truth Layer workflows for AI Life Operations System — Germany Edition.

Current responsibilities:

- expose truth service API boundary
- provide health and Swagger endpoints
- prepare for truth analysis persistence
- prepare for structured AI-based credibility analysis

Future responsibilities:

- analyze pasted online content
- extract claim summary
- calculate trust score
- classify risk level
- suggest verification steps
- store truth analysis history
- prepare for URL/source evidence retrieval

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
8085
```
## Main APIs
```text
GET /api/truth/health
```
## Swagger
```text
http://localhost:8085/swagger-ui.html
```
## Health Check
```text
http://localhost:8085/actuator/health
```
## Gateway Route
```text
/api/truth/** → truth-service
```