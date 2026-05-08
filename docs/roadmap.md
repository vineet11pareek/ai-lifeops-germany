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

## Phase 0 Status

Status: Completed

Phase 0 established the production-grade foundation of the platform.


---


## Phase 1 — Login + Dashboard
Goal:

- create frontend application
- add login page
- add dashboard
- integrate Google OAuth
- connect frontend with api-gateway

### Phase 1 Completion Summary

Phase 1 established the first user-facing flow of the platform.

Completed:

- React + TypeScript frontend application
- Landing/login page
- Dashboard layout
- Google login integration
- Frontend protected route handling
- Frontend to API Gateway integration
- Backend Google ID token verification
- Authenticated `/api/users/me` endpoint
- Auto-create user profile on first login
- Frontend CI pipeline
- Frontend Dockerization with Nginx
- Full Docker Compose local runtime with frontend

Result:

Users can log in with Google, access a protected dashboard, and load authenticated profile data through the API Gateway.

---

## Phase 2 — AI Query Service

Goal:

- create ai-service
- integrate Spring AI
- process basic AI questions
- store query history
- show query history on dashboard
- prepare async Kafka processing

### Phase 2 Completion Summary

Phase 2 introduced the first real AI capability of the platform.

Completed:

- Dedicated `ai-service`
- Spring AI integration
- OpenAI model configuration
- AI chat API
- AI query persistence in PostgreSQL
- AI query history API
- Dashboard AI question form
- Dashboard query history UI
- Kafka foundation for AI completion events
- `ai.query.completed` event publishing
- Error handling and correlation ID in `ai-service`
- Tests and CI for `ai-service`
- Docker runtime validation

Result:

Users can ask AI questions from the dashboard, receive answers, and view persisted query history. The system also publishes AI completion events to Kafka for future task and notification workflows.

---

## Phase 3 — Document Analyzer

Status: In Progress

Goal:

- create document-service
- store document metadata
- analyze pasted document text
- extract summary, deadlines, risks, and actions
- show document analysis in dashboard
- prepare for future PDF/OCR support

---

## Phase 4 — Task Approval Layer

- Create task proposals
- Approve, reject, or edit task
- Track task status

## Phase 5 — Truth Layer

- Analyze URL or text
- Extract claims
- Compare sources
- Generate credibility score