# AI Life Operations System — Germany Edition

AI Life Operations System is a modular AI-powered platform designed to help users in Germany manage bureaucracy, documents, deadlines, online information verification, and task execution with human approval.

## Core Idea

The platform is not just a chatbot. It is an action-oriented AI system that can:

- Analyze official letters and documents
- Extract deadlines and required actions
- Create task proposals
- Ask for user approval before execution
- Help users verify online content using a Truth Layer
- Store user queries, documents, tasks, and history

## Initial Modules

- User & Authentication
- Dashboard
- AI Query History
- Bureaucracy Document Analyzer
- Task Approval Layer
- Truth Layer for Internet Content

## Tech Stack

- Java 21
- Spring Boot
- Spring Cloud Gateway
- Spring AI
- PostgreSQL
- Kafka
- Docker
- GitHub Actions
- React / TypeScript

---

## Local Development Commands

Start core local system:

```bash
docker compose -f infra/local/docker-compose.yml up --build
```
Start local system with tools:
```bash
docker compose -f infra/local/docker-compose.yml --profile tools up --build
```

Stop local system:
```bash
docker compose -f infra/local/docker-compose.yml down
```
---

## Service Documentation

Service-level documentation:

- [user-service](backend/user-service/README.md)
- [api-gateway](backend/api-gateway/README.md)

Reason:

- each service is understandable independently
- improves developer onboarding
- supports microservice ownership
- improves portfolio readability

---

## Current Status

The project is currently completing 

## Phase 0: Project Foundation.

Current backend services:

- api-gateway
- user-service

Current infrastructure:

- PostgreSQL
- Kafka
- pgAdmin optional
- Kafka UI optional

Current capabilities:

- user-service REST APIs
- API Gateway routing
- PostgreSQL persistence
- Flyway migrations
- centralized error handling
- request validation
- Swagger API documentation
- Docker Compose local runtime
- GitHub Actions CI

## Phase 1 Status

Status: Completed

Phase 1 delivered the first end-to-end user-facing flow:

```text
Frontend → Google Login → Protected Dashboard → API Gateway → user-service → PostgreSQL
```
Current frontend capabilities:

- landing page
- Google login
- protected dashboard route
- logout
- profile loading from backend

Current backend auth capability:

- frontend sends Google ID token
- user-service validates token
- user-service creates/loads user profile