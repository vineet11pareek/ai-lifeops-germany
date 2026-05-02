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

---

## Decision 005 — Use API Gateway as Single Entry Point

Status: Accepted

We introduced api-gateway as the single entry point for frontend and external clients.

Reason:

- Frontend should not call internal services directly
- Authentication can be centralized later
- Routing logic stays outside business services
- This matches production microservice patterns

---

## Decision 006 — Use Static Routing Initially

Status: Accepted

The API Gateway currently routes to user-service using a static localhost URL.

Reason:

- Simpler for Phase 0
- Avoids service discovery complexity at the beginning
- Can later evolve to Docker DNS, Eureka, Kubernetes service names, or service mesh

## Decision 007 — Use PostgreSQL as Primary Database

Status: Accepted

PostgreSQL will be used as the primary relational database.

Reason:

- Strong production-grade database
- Works well with Spring Boot and JPA
- Supports structured business data
- Can later support vector search using pgvector

---

## Decision 008 — Use Kafka for Async Processing

Status: Accepted

Kafka will be used for asynchronous workflows.

Reason:

- AI processing may take time
- Document analysis should not block user requests
- Task creation can be event-driven
- Kafka supports scalable distributed processing

---

## Decision 009 — Disable Kafka Auto Topic Creation

Status: Accepted

Kafka topic auto-creation is disabled in local setup.

Reason:

- Prevents accidental topic creation
- Encourages explicit topic management
- Closer to production behavior

## Decision 010 — Use Flyway for Database Migrations

Status: Accepted

Flyway will manage database schema migrations.

Reason:

- Database changes are version-controlled
- Schema changes are repeatable across environments
- Avoids relying on Hibernate automatic table creation
- Matches production-grade database change management

---

## Decision 011 — Use Hibernate Validate Mode

Status: Accepted

Hibernate is configured with `ddl-auto=validate`.

Reason:

- Prevents accidental schema changes by the application
- Ensures entity mappings match the database schema
- Keeps Flyway as the source of truth for schema creation

## Decision 012 — Use Layered Package Structure

Status: Accepted

Each backend service will follow a layered package structure.

Standard layers:

- controller
- service
- repository
- entity
- dto
- exception

Reason:

- Clear separation of concerns
- Easier unit testing
- Easier maintenance
- Better alignment with production Spring Boot applications