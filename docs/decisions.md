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

## Decision 013 — Use Instant for Timestamps

Status: Accepted

We use java.time.Instant for createdAt and updatedAt fields.

Reason:

- Stores timestamps in UTC
- Avoids timezone-related bugs
- Suitable for distributed systems
- Works well with PostgreSQL TIMESTAMP WITH TIME ZONE

## Decision 014 — Use Centralized Exception Handling

Status: Accepted

Each backend service will use centralized exception handling with `@RestControllerAdvice`.

Reason:

- Keeps controllers clean
- Ensures consistent API error responses
- Prevents raw stack traces from being exposed
- Improves frontend integration
- Aligns with production API design

## Decision 015 — Use Standard API Response Wrapper

Status: Accepted

Successful API responses will use a common response wrapper.

Reason:

- Consistent frontend integration
- Easier API consumption
- Common structure across services

---

## Decision 016 — Validate Requests at API Boundary

Status: Accepted

API request DTOs will use Jakarta Bean Validation annotations.

Reason:

- Prevents invalid data from entering business logic
- Keeps validation close to input model
- Improves API reliability and security

## Decision 017 — Add Tests Before CI/CD

Status: Accepted

Automated tests are added before GitHub Actions CI/CD.

Reason:

- CI pipeline should validate real behavior
- Prevents broken code from being merged
- Supports production-grade development workflow

## Decision 018 — Use GitHub Actions for CI

Status: Accepted

GitHub Actions will be used for continuous integration.

Reason:

- Native integration with GitHub
- Easy to configure per service
- Supports test, build, Docker image creation, and deployment workflows
- Good fit for portfolio and production-style development

## Decision 019 — Dockerize Backend Services

Status: Accepted

Backend services will be packaged as Docker containers.

Reason:

- Consistent runtime across environments
- Easier local orchestration
- Prepares services for cloud deployment
- Aligns with microservice deployment practices

---

## Decision 020 — Use Environment-Based Service Configuration

Status: Accepted

Service URLs and database connections are provided using environment variables.

Reason:

- Avoids hardcoded environment-specific values
- Enables local, Docker, CI, and cloud deployment with the same code
- Supports twelve-factor application principles

## Decision 021 — Use Multi-Stage Docker Builds

Status: Accepted

Backend services use multi-stage Dockerfiles.

Reason:

- Separates build-time dependencies from runtime image
- Reduces final image size
- Improves build repeatability
- Makes Docker builds suitable for CI/CD pipelines

## Decision 022 — Use Spring Boot Actuator for Health Checks

Status: Accepted

Backend services expose health endpoints using Spring Boot Actuator.

Reason:

- Provides standard operational endpoints
- Supports local Docker health checks
- Prepares services for Kubernetes readiness and liveness probes
- Helps monitoring and alerting systems verify service health

## Decision 023 — Use Correlation ID for Request Tracking

Status: Accepted

Backend services will support `X-Correlation-Id`.

Reason:

- Helps trace one request across multiple services
- Improves debugging in distributed systems
- Can be connected with future OpenTelemetry trace IDs
- Supports better production support and incident investigation

## Decision 024 — Use OpenAPI for REST API Documentation

Status: Accepted

Backend services will expose OpenAPI documentation.

Reason:

- Provides a standard API contract
- Makes APIs easier to test and share
- Helps frontend teams understand backend APIs
- Improves maintainability and interview presentation

## Decision 025 — Use Docker Compose Profiles for Local Tools

Status: Accepted

Optional local tools such as pgAdmin and Kafka UI are started using the `tools` Docker Compose profile.

Reason:

- Keeps default local startup lightweight
- Separates runtime services from debugging tools
- Improves developer experience
- Keeps local setup closer to production thinking

## Decision 026 — Maintain Service-Level README Files

Status: Accepted

Each backend service will include its own README file.

Reason:

- Documents service responsibility clearly
- Helps developers run and test services independently
- Supports microservice ownership
- Improves maintainability and onboarding

## Decision 027 — Use React with TypeScript for Frontend

Status: Accepted

The frontend will use React with TypeScript and Vite.

Reason:

- Fast development setup
- Strong typing with TypeScript
- Good ecosystem support
- Easy integration with OAuth and API Gateway

## Decision 028 — Use Google OAuth as First Login Provider

Status: Accepted

Google OAuth is implemented as the first login provider.

Reason:

- widely used authentication provider
- good support for OpenID Connect
- suitable for fast onboarding
- prepares the platform for future Facebook login

## Decision 029 — Frontend Communicates Only Through API Gateway

Status: Accepted

The frontend will call backend APIs only through `api-gateway`.

Reason:

- hides internal service topology
- centralizes routing
- prepares for authentication and authorization enforcement
- aligns with microservice architecture patterns

## Decision 030 — Verify Google ID Token on Backend

Status: Accepted

Google ID tokens are verified in the backend before user data is returned or created.

Reason:

- frontend tokens cannot be trusted without backend verification
- verifies token audience, issuer, expiry, and signature
- prevents unauthorized access using fake tokens
- aligns with secure authentication practice

## Decision 031 — Use Frontend Protected Routes

Status: Accepted

Private frontend pages are wrapped with a protected route component.

Reason:

- Prevents unauthenticated navigation to dashboard
- Keeps routing logic centralized
- Improves frontend maintainability
- Complements backend token validation

## Decision 032 — Add Frontend CI Pipeline

Status: Accepted

The frontend application has a dedicated GitHub Actions CI workflow.

Reason:

- Validates frontend build on every relevant change
- Detects TypeScript and bundling errors early
- Keeps frontend quality aligned with backend CI
- Prepares for future Docker image build and deployment

## Decision 033 — Serve Frontend with Nginx Container

Status: Accepted

The React frontend is built with Node and served from an Nginx container.

Reason:

- separates build-time dependencies from runtime
- serves optimized static assets
- supports React Router fallback
- matches common production deployment patterns