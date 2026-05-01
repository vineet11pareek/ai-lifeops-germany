# Architecture Decisions

## Decision 001 — Use Monorepo

Status: Accepted

We will use a monorepo for the initial development of AI Life Operations System.

Reason:

- Easier to manage during early development
- Backend, frontend, infra, and docs stay in one place
- Good for portfolio and interview demonstration
- CI/CD can be managed centrally

## Decision 002 — Start Small, Scale Later

Status: Accepted

We will not create all microservices at once.

Initial services:

- user-service
- api-gateway

Reason:

- Avoid overengineering
- Build working user journey first
- Add Kafka and async processing after basic flow works

## Decision 003 — Start with user-service

Status: Accepted

We started implementation with user-service as the first microservice.

Reason:

- Every system requires user context
- Future authentication and authorization will depend on user-service
- Enables early testing of APIs
- Provides base for dashboard integration

---

## Decision 004 — Use Mock Data Initially

Status: Accepted

We are using mock user data instead of database integration in Phase 0.

Reason:

- Focus on system structure first
- Avoid premature complexity
- Database integration will be added in later phase