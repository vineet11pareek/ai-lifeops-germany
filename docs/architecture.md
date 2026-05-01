# Architecture — AI Life Operations System (Germany Edition)

## Overview

AI Life Operations System is a distributed platform designed to help users manage documents, tasks, and decisions using AI.

The system follows a microservices-based architecture with a centralized API Gateway.

---

## High-Level Architecture

```text
Frontend (React)
        ↓
API Gateway (Spring Cloud Gateway)
        ↓
Backend Services
        ↓
Database / Messaging / AI Layer

```
---

## Phase 0

### 1. user-service
Responsible for:
* User Profile
* User related API
* Future Integration

Base Endpoints:
* /api/users/health
* /api/users/me

### 2. api-gateway

Responsible for:

- Acting as the single entry point for frontend requests
- Routing user-related APIs to user-service
- Preparing the platform for future authentication and rate limiting

Current Route:

```text
/api/users/**  →  user-service

ports:
api-gateway: 8080
user-service: 8081
```


Current limitations:

```md
## Current Limitations

- No authentication
- No database persistence
- No async processing
- Gateway currently uses static service URL