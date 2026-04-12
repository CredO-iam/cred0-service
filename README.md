# CredØ

Lightweight, extensible IAM tooling for managing identities, roles, groups, and OAuth2/OpenID Connect clients.

## What this project is

`CredØ` is designed as a compact IAM platform that can evolve from a simple internal access management service into a full authorization core for custom applications and APIs.

Based on the current repository structure, the project consists of two main parts:

- `admin-ui` — an administrative interface built with `Vue 3 + Vite`
- `core` — a backend built with `Spring Boot + Spring Authorization Server + Spring Data JPA`

The idea is already visible in the codebase: instead of a heavy platform with a large dependency surface, the project uses a clear modular core where IAM entities and OAuth2 client management are isolated into dedicated packages and can evolve independently.

## Project intent

From a product perspective, `CredØ` is a foundation for the following scenarios:

- user management (`users`)
- group management (`groups`)
- role management (`roles`)
- registered OAuth2 client management (`registered clients`)
- future expansion toward fuller OAuth2 / OpenID Connect support on top of Spring Authorization Server

In practice, the project combines two roles:

1. **IAM admin layer** — CRUD operations and administration for core access entities.
2. **Authorization layer** — OAuth2/OIDC infrastructure where clients, tokens, and security flows are backed by Spring Authorization Server.

## Why it already looks like a lightweight, extensible IAM core

In its current implementation, the project shows several characteristics of a lightweight and extensible IAM foundation:

- **Small technology surface**: Vue on the frontend and Spring Boot on the backend, without extra platform overhead.
- **Clear backend modularity**: `users`, `groups`, `roles`, `clients`, `security`, and `bootstrap` are separated by responsibility.
- **Explicit internal layers**: `api`, `service`, `persistence`, and `mappers`.
- **Simple persistence model**: the project currently uses in-memory `H2` with `JPA`, which keeps local development and prototyping fast.
- **Domain-level extensibility**: new IAM entities can be added next to the existing modules.
- **UI-level extensibility**: the admin interface is already organized as separate domain screens.

This is not a boxed enterprise IAM suite. It is a focused core that is easy to evolve toward project-specific requirements.

## Current architecture

### Frontend

The `admin-ui` module already includes screens for:

- `Settings`
- `Clients`
- `Users`
- `Groups`
- `Roles`

The interface is built with `Vue 3`, bundled with `Vite`, and works as a standalone admin panel that communicates with the backend over REST.

### Backend

The `core` module is a Spring Boot application running on Java 21.

Key backend modules:

- `io.cred0.core.users` — users
- `io.cred0.core.groups` — groups
- `io.cred0.core.roles` — roles
- `io.cred0.core.clients` — registered OAuth2 clients
- `io.cred0.core.security` — security configuration
- `io.cred0.core.bootstrap` — initial system administrator bootstrap

The backend is structured around REST APIs for administrative operations and around Spring Authorization Server integration for authorization server runtime capabilities.

## What is already implemented

### 1. OAuth2 client management

The project includes a dedicated client management module with the following REST API:

- `GET /api/registered-clients`
- `GET /api/registered-clients/{id}`
- `GET /api/registered-clients/by-client-id/{clientId}`
- `POST /api/registered-clients`
- `DELETE /api/registered-clients/{id}`

Clients are stored through a custom JPA-backed adapter around `RegisteredClientRepository`, with persistence in the `oauth2_registered_client` table.

This is an important part of the project vision: OAuth2 clients are not hidden framework state, but a managed domain concept inside the IAM system.

### 2. User management

There is a full REST CRUD API for users through `UserController` (`/api/users`).

The user model already includes:

- `username`
- `firstName` / `lastName`
- `email`
- `enabled`
- `emailVerified`
- arbitrary `attributes`
- credentials (`type` and `value`)

This provides a solid base for an internal user directory.

### 3. Group management

There is a REST CRUD API for groups through `GroupController` (`/api/groups`).

Groups support:

- name and description
- links to users
- links to roles

This forms a dedicated layer for collective access management.

### 4. Role management

There is a REST CRUD API for roles through `RoleController` (`/api/roles`).

Roles support:

- name and description
- links to users
- links to groups

Together with groups, this establishes a solid RBAC foundation.

### 5. System administrator bootstrap

At backend startup, `AdminBootstrapInitializer` ensures that a minimal administrative baseline exists:

- the `system_admin` role
- the `admins` group
- the `admin` user (or a configured username)
- links between those entities

This is a strong IAM-first signal: the system brings up a minimum viable administrative control plane from the start.

## Repository layout

| Path | Purpose |
| --- | --- |
| `admin-ui/` | Vue 3 / Vite single-page admin UI |
| `core/` | Spring Boot backend and authorization core |
| `core/doc/SPEC.md` | Current specification for the implemented client management module |
| `worklog/` | Task planning notes and execution history |

## Technology stack

### Frontend

- `Vue 3`
- `Vite`
- `Tailwind CSS 4`

### Backend

- `Java 21`
- `Spring Boot 4.0.5`
- `Spring Security OAuth2 Authorization Server`
- `Spring Data JPA`
- `H2 Database`

## Important context about the current state

From an engineering perspective, the project is already shaped as an **IAM core**, but it still looks like a **growing foundation**, not a finished production-ready product.

That is visible from several current characteristics:

- the project uses an in-memory `H2` database (`jdbc:h2:mem:cred0`)
- the schema is recreated on startup (`ddl-auto: create`)
- the `H2 console` is explicitly exposed
- `core/doc/SPEC.md` currently documents only the registered clients module in detail
- security rules for the administrative REST endpoints still appear minimal and are not yet a complete authorization model for the admin API

In other words, the project is already a strong fit for:

- prototyping an IAM core
- developing a custom user / role / group model
- experimenting with OAuth2/OpenID Connect on top of Spring Authorization Server
- building a custom admin console on top of a straightforward REST API

For a production-grade setup, the natural next steps would likely include:

- persistent storage instead of in-memory H2
- stronger protection for the admin API
- more explicit OIDC configuration and end-user authentication flows
- auditing, access policies, lifecycle operations, and integrations

## Extension points

The project can be expanded in several natural directions.

### 1. New IAM entities

Following the `users/groups/roles/clients` pattern, future modules could include:

- permissions
- realms / tenants
- identity providers
- sessions
- consent management
- policy objects

### 2. Authorization server evolution

On top of Spring Authorization Server, the project can evolve toward:

- OpenID Connect user flows
- additional grant types and client settings
- claims mapping
- token customization
- consent and scope management

### 3. Admin UI evolution

The frontend already follows a clear pattern of separate domain sections, which makes it straightforward to extend with:

- additional entity pages
- richer edit forms
- search, filtering, and paging
- deeper entity relationships
- system settings and observability

## Quick start for local development

The commands below reflect the current repository structure and checked-in configuration.

### Backend

From `core/`:

```zsh
./gradlew bootRun
```

The bootstrap administrator can be overridden with these environment variables:

- `CRED0_BOOTSTRAP_ADMIN_USERNAME`
- `CRED0_BOOTSTRAP_ADMIN_PASSWORD`

### Frontend

From `admin-ui/`:

```zsh
npm install
npm run dev
```

The frontend uses `VITE_API_BASE_URL`, so the backend API address can be adjusted through Vite environment variables when needed.

## Overall assessment of the project direction

Looking at the repository as a product idea, `CredØ` already presents itself as a **clear, minimal IAM core** with:

- an administrative UI
- backend domains for identity and access management
- integration with the OAuth2 authorization server model
- a solid RBAC and client management base
- natural growth paths toward a fuller OAuth2/OpenID Connect IAM solution

Its current strength is **architectural clarity**: it is small enough to evolve quickly, and structured well enough to avoid turning into a chaotic monolith as new capabilities are added.
