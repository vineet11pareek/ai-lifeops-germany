# document-service

## Responsibility

`document-service` manages document analysis workflows for AI Life Operations System — Germany Edition.

Current responsibilities:

- expose document service API boundary
- provide health and Swagger endpoints
- prepare for document metadata persistence
- prepare for AI-based document analysis

Future responsibilities:

- store document metadata
- analyze pasted document text
- support PDF upload
- support OCR processing
- store analysis results
- publish document analysis events

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
8083
```
Main APIs:
```text
GET /api/documents/health
```

Swagger:
```text
http://localhost:8083/swagger-ui.html
```

Health Check:
```text
http://localhost:8083/actuator/health
```

Gateway Route:
```text
/api/documents/** → document-service
```

