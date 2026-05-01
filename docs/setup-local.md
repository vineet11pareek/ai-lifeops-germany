# Local Setup Guide

This guide explains how to run the project on your local machine.

---

## 1. What you need before starting

Install these tools:

- Java 21
- Maven
- Git
- Docker
- Docker Compose
- Node.js (for frontend later)

---

## 2. Clone the project

```bash
git clone https://github.com/YOUR_USERNAME/ai-lifeops-germany.git
cd ai-lifeops-germany
````
---

## 3. Project Folder Structure

+ ai-lifeops-germany/
    + backend/        → all backend services (Spring Boot)
    + frontend/       → UI application (React later)
    + infra/          → Docker, Kafka, PostgreSQL setup
    + docs/           → documentation files
    + .github/        → CI/CD pipelines
    + README.md       → project overview

---
## 4. What working now

Currently working:

- user-service runs on port 8081
- api-gateway runs on port 8080
- Gateway routes `/api/users/**` to user-service

Next steps will include:

+ Creating first backend service (user-service)
+ Adding API Gateway
+ Adding Docker setup
+ Running services locally

---
## 5. How we will run the project (later)

For now, run services manually:

Terminal 1:

```bash
cd backend/user-service
mvnw.cmd spring-boot:run

````

Terminal 2:

```bash
cd backend/api-gateway
mvnw.cmd spring-boot:run
````

Test through gateway:

http://localhost:8080/api/users/health
http://localhost:8080/api/users/me

````
docker compose up
````

---
## 6. Current status

Right now:

❌ No services are running

❌ No backend is created

❌ No frontend is created

We are still in foundation setup phase


---
## 7. Next steps


---

# ✅ After adding

Run:

```bash
git add docs/setup-local.md
git commit -m "Fix setup-local documentation"
git push
