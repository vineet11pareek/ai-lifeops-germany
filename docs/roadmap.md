# Roadmap

## Phase 0 — Project Foundation

- Create repository structure
- Add documentation
- Create first Spring Boot service
- Create API Gateway
- Add Docker Compose
- Add basic CI pipeline

## Phase 0 Completion Summary

Phase 0 established the production-grade foundation of the platform.

Completed:

- Monorepo project structure
- Base documentation
- user-service
- api-gateway
- PostgreSQL local infrastructure
- Kafka local infrastructure
- Flyway database migration
- Layered backend package structure
- Standard API response model
- Centralized exception handling
- Request validation
- Basic automated tests
- GitHub Actions CI pipeline
- Dockerization with multi-stage Docker builds
- Docker Compose local orchestration
- Docker Compose profiles for optional tools
- Spring Boot Actuator health checks
- OpenAPI/Swagger documentation
- Correlation ID groundwork
- Service-level README files

Result:

The platform is ready to move into Phase 1: Login + Dashboard.

---


## Phase 1 — Login + Dashboard

- Google login
- User profile
- Dashboard page
- Query history

## Phase 2 — AI Query Service

- Ask AI questions
- Store query and response
- Display history

## Phase 3 — Document Analyzer

- Upload PDF or image
- Extract text
- Analyze document
- Extract actions and deadlines

## Phase 4 — Task Approval Layer

- Create task proposals
- Approve, reject, or edit task
- Track task status

## Phase 5 — Truth Layer

- Analyze URL or text
- Extract claims
- Compare sources
- Generate credibility score