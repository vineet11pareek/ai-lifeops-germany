# Architecture Decisions

## Decision 001 — Use Monorepo

Status: Accepted

We will use a monorepo for the initial development of AI Life Operations System.

Reason:

- Easier to manage during early development
- Backend, frontend, infra, and docs stay in one place
- Good for portfolio and interview demonstration
- CI/CD can be managed centrally

---

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

---

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

---

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

---

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

---

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

---

## Decision 013 — Use Instant for Timestamps

Status: Accepted

We use java.time.Instant for createdAt and updatedAt fields.

Reason:

- Stores timestamps in UTC
- Avoids timezone-related bugs
- Suitable for distributed systems
- Works well with PostgreSQL TIMESTAMP WITH TIME ZONE

---

## Decision 014 — Use Centralized Exception Handling

Status: Accepted

Each backend service will use centralized exception handling with `@RestControllerAdvice`.

Reason:

- Keeps controllers clean
- Ensures consistent API error responses
- Prevents raw stack traces from being exposed
- Improves frontend integration
- Aligns with production API design

---

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

---

## Decision 017 — Add Tests Before CI/CD

Status: Accepted

Automated tests are added before GitHub Actions CI/CD.

Reason:

- CI pipeline should validate real behavior
- Prevents broken code from being merged
- Supports production-grade development workflow

---

## Decision 018 — Use GitHub Actions for CI

Status: Accepted

GitHub Actions will be used for continuous integration.

Reason:

- Native integration with GitHub
- Easy to configure per service
- Supports test, build, Docker image creation, and deployment workflows
- Good fit for portfolio and production-style development

---

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

---

## Decision 021 — Use Multi-Stage Docker Builds

Status: Accepted

Backend services use multi-stage Dockerfiles.

Reason:

- Separates build-time dependencies from runtime image
- Reduces final image size
- Improves build repeatability
- Makes Docker builds suitable for CI/CD pipelines

---

## Decision 022 — Use Spring Boot Actuator for Health Checks

Status: Accepted

Backend services expose health endpoints using Spring Boot Actuator.

Reason:

- Provides standard operational endpoints
- Supports local Docker health checks
- Prepares services for Kubernetes readiness and liveness probes
- Helps monitoring and alerting systems verify service health

---

## Decision 023 — Use Correlation ID for Request Tracking

Status: Accepted

Backend services will support `X-Correlation-Id`.

Reason:

- Helps trace one request across multiple services
- Improves debugging in distributed systems
- Can be connected with future OpenTelemetry trace IDs
- Supports better production support and incident investigation

---

## Decision 024 — Use OpenAPI for REST API Documentation

Status: Accepted

Backend services will expose OpenAPI documentation.

Reason:

- Provides a standard API contract
- Makes APIs easier to test and share
- Helps frontend teams understand backend APIs
- Improves maintainability and interview presentation

---

## Decision 025 — Use Docker Compose Profiles for Local Tools

Status: Accepted

Optional local tools such as pgAdmin and Kafka UI are started using the `tools` Docker Compose profile.

Reason:

- Keeps default local startup lightweight
- Separates runtime services from debugging tools
- Improves developer experience
- Keeps local setup closer to production thinking

---

## Decision 026 — Maintain Service-Level README Files

Status: Accepted

Each backend service will include its own README file.

Reason:

- Documents service responsibility clearly
- Helps developers run and test services independently
- Supports microservice ownership
- Improves maintainability and onboarding

---

## Decision 027 — Use React with TypeScript for Frontend

Status: Accepted

The frontend will use React with TypeScript and Vite.

Reason:

- Fast development setup
- Strong typing with TypeScript
- Good ecosystem support
- Easy integration with OAuth and API Gateway

---

## Decision 028 — Use Google OAuth as First Login Provider

Status: Accepted

Google OAuth is implemented as the first login provider.

Reason:

- widely used authentication provider
- good support for OpenID Connect
- suitable for fast onboarding
- prepares the platform for future Facebook login

---

## Decision 029 — Frontend Communicates Only Through API Gateway

Status: Accepted

The frontend will call backend APIs only through `api-gateway`.

Reason:

- hides internal service topology
- centralizes routing
- prepares for authentication and authorization enforcement
- aligns with microservice architecture patterns

---

## Decision 030 — Verify Google ID Token on Backend

Status: Accepted

Google ID tokens are verified in the backend before user data is returned or created.

Reason:

- frontend tokens cannot be trusted without backend verification
- verifies token audience, issuer, expiry, and signature
- prevents unauthorized access using fake tokens
- aligns with secure authentication practice

---

## Decision 031 — Use Frontend Protected Routes

Status: Accepted

Private frontend pages are wrapped with a protected route component.

Reason:

- Prevents unauthenticated navigation to dashboard
- Keeps routing logic centralized
- Improves frontend maintainability
- Complements backend token validation

---

## Decision 032 — Add Frontend CI Pipeline

Status: Accepted

The frontend application has a dedicated GitHub Actions CI workflow.

Reason:

- Validates frontend build on every relevant change
- Detects TypeScript and bundling errors early
- Keeps frontend quality aligned with backend CI
- Prepares for future Docker image build and deployment

---

## Decision 033 — Serve Frontend with Nginx Container

Status: Accepted

The React frontend is built with Node and served from an Nginx container.

Reason:

- separates build-time dependencies from runtime
- serves optimized static assets
- supports React Router fallback
- matches common production deployment patterns

---

## Decision 034 — Complete Phase 1 with Google Login and Dashboard

Status: Accepted

Phase 1 is completed once the user can log in with Google and view an authenticated dashboard.

Reason:

- establishes the first real user-facing flow
- validates frontend-backend integration
- confirms authenticated profile loading
- prepares platform for AI modules in later phases

---

## Decision 035 — Create Dedicated AI Service

Status: Accepted

AI functionality will be implemented in a dedicated `ai-service`.

Reason:

- AI workloads may have higher latency and cost
- AI service can scale independently
- provider keys and model configuration stay isolated
- future modules such as document analysis and Truth Layer can reuse the same AI foundation

---

## Decision 036 — Add ai-service as Dedicated AI Boundary

Status: Accepted

A new `ai-service` is added as the dedicated backend service for AI capabilities.

Reason:

- keeps AI concerns separate from user management
- supports independent scaling
- prepares for Spring AI integration
- provides reusable foundation for future AI modules

---

## Decision 037 — Use Spring AI ChatClient for AI Calls

Status: Accepted

`ai-service` uses Spring AI ChatClient for LLM communication.

Reason:

- Spring-native abstraction over AI providers
- keeps AI provider logic isolated
- supports future provider switching
- prepares for structured outputs, RAG, tools, and observability

---

## Decision 038 — Standardize Error Handling in ai-service

Status: Accepted

`ai-service` uses centralized exception handling and standard error response format.

Reason:

- keeps API behavior consistent with user-service
- prevents internal AI provider errors from leaking to clients
- improves frontend error handling
- supports production troubleshooting with correlation IDs

---

## Decision 039 — Persist AI Query History

Status: Accepted

AI queries and responses are stored in PostgreSQL.

Reason:

- users need query history
- AI failures should be traceable
- supports audit and debugging
- prepares the system for async AI workflows

---

## Decision 040 — Use Service-Specific Flyway History Tables

Status: Accepted

Each service uses its own Flyway schema history table in the shared local PostgreSQL database.

Reason:

- prevents migration version conflicts between services
- keeps local setup simple
- prepares for future service-owned schemas or databases

---

## Decision 041 — Publish AI Query Completion Events

Status: Accepted

`ai-service` publishes an event when an AI query is completed.

Reason:

- enables future task-service and notification-service integration
- supports event-driven architecture
- avoids tight coupling between services
- prepares for async AI processing

---

## Decision 042 — Use Query ID as Kafka Message Key

Status: Accepted

AI query events use `queryId` as the Kafka message key.

Reason:

- keeps events for the same query ordered within Kafka partitioning
- provides deterministic event routing
- supports future query lifecycle events

---

## Decision 043 — Mock AI Provider Interactions in Tests

Status: Accepted

Automated tests for `ai-service` do not call the real AI provider.

Reason:

- avoids external dependency in CI
- prevents API cost during tests
- keeps tests deterministic
- improves build reliability

---

## Decision 044 — Add Dedicated CI for ai-service

Status: Accepted

`ai-service` has its own GitHub Actions CI workflow.

Reason:

- validates AI service independently
- keeps service-specific quality gates clear
- supports microservice-level CI ownership

---

## Decision 045 — Complete Phase 2 with Synchronous AI Query Flow

Status: Accepted

Phase 2 is completed with a synchronous AI query flow and persisted query history.

Reason:

- validates the first real AI capability
- keeps initial user experience simple
- creates foundation for async processing later
- confirms frontend, gateway, AI service, database, and Kafka integration

---

## Decision 046 — Start Document Analyzer with Text Input

Status: Accepted

The first Document Analyzer version will analyze pasted document text instead of PDF/image upload.

Reason:

- validates analysis workflow faster
- avoids early OCR/file-storage complexity
- allows prompt and response format to stabilize first
- PDF and OCR support can be added after the core analysis flow works

---

## Decision 047 — Create Dedicated document-service

Status: Accepted

Document analysis is handled by a dedicated `document-service`.

Reason:

- document lifecycle has its own business model and statuses
- prepares for future file upload, OCR, and storage
- keeps ai-service focused on AI provider interaction
- supports independent scaling and ownership

---

## Decision 048 — Add document-service as Dedicated Document Boundary

Status: Accepted

A new `document-service` is added as the dedicated backend service for document workflows.

Reason:

- keeps document lifecycle separate from AI provider logic
- supports future file upload and OCR workflows
- allows independent scaling and ownership
- keeps the platform modular

---

## Decision 049 — Persist Document Metadata Before AI Analysis

Status: Accepted

Documents are stored before AI analysis is added.

Reason:

- each document receives a stable ID
- document lifecycle can be tracked
- failed analysis can still be linked to original input
- prepares for async document analysis later

---

## Decision 050 — Delegate AI Reasoning to ai-service

Status: Accepted

`document-service` calls `ai-service` for document analysis instead of calling the AI provider directly.

Reason:

- keeps AI provider logic centralized
- avoids duplicating Spring AI logic in every service
- keeps document-service focused on document lifecycle
- supports future reuse by Truth Layer and task modules

---

## Decision 051 — Use Dedicated Structured Endpoint for Document Analysis

Status: Accepted

Document analysis uses a dedicated `ai-service` endpoint instead of the generic chat API.

Reason:

- generic chat output is not reliable enough for workflow automation
- document-service needs structured data
- prompt and parsing logic should stay inside ai-service
- improves service contract clarity

---

## Decision 052 — Publish Document Analyzed Events

Status: Accepted

`document-service` publishes an event after successful document analysis.

Reason:

- enables future task-service and notification-service workflows
- avoids direct coupling between document-service and downstream services
- supports event-driven architecture
- prepares for approval-based task creation

---

## Decision 053 — Use Document ID as Kafka Message Key

Status: Accepted

Document analysis events use `documentId` as the Kafka message key.

Reason:

- keeps events for the same document ordered within Kafka partitioning
- provides deterministic event routing
- supports future document lifecycle event processing

---

## Decision 054 — Mock Downstream AI and Kafka in document-service Tests

Status: Accepted

`document-service` tests mock downstream AI and Kafka interactions.

Reason:

- avoids external dependencies in CI
- prevents flaky tests
- keeps tests fast
- validates document-service behavior independently

---

## Decision 055 — Add Dedicated CI for document-service

Status: Accepted

`document-service` has its own GitHub Actions CI workflow.

Reason:

- validates document-service independently
- supports microservice-level quality gates
- keeps service ownership clear

---

## Decision 056 — Complete Phase 3 with Text-Based Document Analyzer

Status: Accepted

Phase 3 is completed with a text-based Document Analyzer.

Reason:

- validates the main user value before adding PDF/OCR complexity
- enables structured summary, deadline, action, risk, and next step extraction
- creates a foundation for future Bureaucracy Assistant workflows
- prepares the platform for task approval and reminder features

---

## Decision 057 — Introduce Human-in-the-Loop Task Approval

Status: Accepted

AI-detected actions will become task proposals that require user approval.

Reason:

- avoids unsafe autonomous execution
- keeps user in control
- supports legal and administrative workflows
- creates a foundation for future execution agents

---

## Decision 058 — Use Kafka for Task Proposal Creation

Status: Accepted

Task proposals will be created from Kafka events such as `document.analyzed`.

Reason:

- keeps document-service decoupled from task-service
- supports event-driven workflows
- allows future services to react to the same event
- improves scalability and maintainability

---

## Decision 059 — Add task-service as Dedicated Task Approval Boundary

Status: Accepted

A new `task-service` is added as the dedicated backend service for task proposals and approval workflows.

Reason:

- separates task lifecycle from document analysis
- supports human-in-the-loop approval
- prepares for future task execution agents
- allows independent scaling and ownership

---

## Decision 060 — Persist Task Proposals

Status: Accepted

Task proposals are persisted in PostgreSQL.

Reason:

- approval workflow requires durable state
- users need to view pending and historical tasks
- future execution agents require task lifecycle tracking
- supports auditability and transparency

---

