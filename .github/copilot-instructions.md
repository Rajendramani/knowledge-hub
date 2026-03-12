## Quick orientation — Knowledge Hub

This repository is a mono-repo for a microservices-based Knowledge Hub. Key points an AI coding agent should know to be immediately productive:

- Architecture: see `docs/architecture/01-system-architecture.mermaid` (API Gateway, Keycloak, multiple Spring Boot microservices, Kafka event bus, Postgres/Mongo/Redis/Elasticsearch, observability stack).
- Build system: Maven parent POM at `pom.xml` (Spring Boot parent 3.3.7). Services live under `services/` and the frontend under `frontend/knowledge-hub-ui`.
- Infra: development notes and composition live under `infrastructure/` (`docker-compose.yml`, `docker-compose.override.yml`, `k8s/`, `helm/`). These files also contain high-level infra notes used by developers.

## What to do first

1. Read `docs/architecture/*.mermaid` for the big-picture service boundaries and data flows (example: ports, data stores, and event consumers listed in `01-system-architecture.mermaid`).
2. Inspect `pom.xml` to see the parent Spring Boot configuration and Java/Spring baseline.
3. Look into `infrastructure/docker-compose*.yml` for local dev expectations (the files contain notes about Keycloak, Kafka/RabbitMQ, Zipkin, ELK, etc.).

## Short actionable checklist (examples)

- Build the top-level project (installs parent artifacts):

  ```powershell
  mvn -f pom.xml clean install
  ```

- Run a single service when its module exists (Spring Boot):

  ```powershell
  mvn -pl services/<service-name> -am spring-boot:run
  ```

- Start infra pieces for local testing (if `docker-compose.yml` is populated):

  ```powershell
  docker-compose -f infrastructure/docker-compose.yml up --build
  ```

Note: some service folders are scaffolds; verify the presence of a service `pom.xml` before running `mvn -pl`.

## Project-specific conventions and patterns

- Bounded contexts: each domain lives under `services/` (e.g., `user-service`, `article-service`, `search-service`). Expect DDD patterns (entities, repositories, domain events).
- Events: services publish domain events to Kafka (see architecture diagram labels like `NoteCreated`, `NoteUpdated`). Use `common-messaging/` and `common-models/` for shared DTOs if present.
- Ports and responsibilities: the mermaid doc lists intended ports per service (e.g., `user-service` 8081, `article-service` 8082). Use those as defaults in local runs.
- Observability: tracing via Zipkin and metrics via Prometheus/Grafana are part of the stack—look for Zipkin/Prometheus config in `infrastructure/` or service `application.yml` files.

## Integration points and external deps

- Identity: Keycloak (OAuth2/OIDC) — integration is at the gateway layer. See `docs/architecture` for expected flows.
- Messaging: Kafka (or RabbitMQ) — domain events and CQRS-style commands.
- Storage: Postgres (transactional), MongoDB (comments/AI memory), Redis (cache/rate-limit), Elasticsearch (search index), S3 (media storage). Mapping is in `docs/architecture/01-system-architecture.mermaid`.

## Helpful repository locations to reference in code edits

- `pom.xml` — parent configuration, Spring Boot version.
- `docs/architecture/*.mermaid` — service boundaries, ports, messaging flows.
- `infrastructure/docker-compose.yml` and `docker-compose.override.yml` — local dev infra notes and services to bring up.
- `services/` — per-service code (look for `pom.xml`, `src/main/java`, and `application.yml` within each service).
- `shared/common-messaging/`, `shared/common-models/` — shared DTOs/message contracts (use these to add or evolve events).

## When creating or changing services

- Follow the existing pattern: add a Maven module under `services/`, align its Spring Boot parent to the root `pom.xml`, and register domain events in `shared/common-messaging` when they cross service boundaries.
- Keep public contracts (events, DTOs) in `shared/common-models` to avoid tight coupling.

## If anything is unclear

- Point to the exact file or diagram and I will refine this guidance. After you review, tell me which developer tasks you want the agent to prioritize (e.g., scaffold a new service, implement an event handler, or wire Keycloak). 

---
Small, focused, actionable guidance tailored to the current repository structure and discoverable docs. Ask for changes or add examples you'd like included.
