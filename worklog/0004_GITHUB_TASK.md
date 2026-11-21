# 0004 · Define CredØ MVP boundaries and acceptance contract

[Task plan](0004_TASK_PLAN.md) · [MVP contract](../PRODUCT_CONTEXT.md#43-first-mvp-boundary)

## Background / Context

`ENGINEERING_HARNESS.md` defines the reusable engineering harness, while
`PRODUCT_CONTEXT.md` must define the first meaningful CredØ milestone.
Existing repository work provides administrative CRUD foundations, but the
product requires an explicit OAuth2/OIDC journey and release boundary before
protocol implementation continues.

This task establishes the product boundary before implementation continues.
It does not implement the OAuth2 flow or change application code.

## Goal

Define a focused MVP in which:

1. a developer can deploy the server with PostgreSQL;
2. the developer can create a user and OAuth2 client through the Admin API;
3. an end user can complete Authorization Code with PKCE;
4. the client can receive tokens and verify the ID Token signature using JWKS;
5. the client can call UserInfo;
6. the system demonstrates secure persistence, operations, testing, and
   documentation suitable for the first usable milestone.

## Scope

### In Scope

- REST-first Admin API for users, clients, scopes, and roles.
- User identifier, username/email, password hash, enabled state, and
  extensible attributes.
- Client identifier, protected secret where applicable, redirect URIs, grant
  types, scopes, and enabled state.
- Scopes `openid`, `profile`, and `email`, plus roles only.
- Authorization Code with PKCE, including login, user authorization, code
  exchange, token issuance and refresh, and redirect URI, client, code, and
  PKCE validation.
- Client Credentials for machine-to-machine integrations.
- OIDC discovery, JWKS, ID Token, and UserInfo.
- ID Token, access-token, and UserInfo claim contracts defined in
  `PRODUCT_CONTEXT.md`.
- PostgreSQL persistence, RSA signing keys, external secret configuration,
  Actuator health/info/metrics, and basic authentication/client/token audit
  events.
- Docker Compose deployment, configuration example, quick start, Admin API
  guide, and Spring Boot/SPA integration examples.
- Unit tests for new domain and service logic.
- Full OAuth2/OIDC integration tests plus negative cases for invalid redirect
  URI, invalid client, expired code, invalid PKCE verifier, invalid token, and
  disabled user.

### Out of Scope

- Device Authorization, Token Exchange, CIBA, JWT Bearer Grant, implicit, and
  Resource Owner Password Credentials.
- Admin UI is a follow-up milestone after the REST-first MVP.
- Groups, permission hierarchy, fine-grained authorization, and policy engine.
- SAML, multi-tenancy, LDAP, federation, social login, MFA, passwordless,
  workflow engine, plugin marketplace, enterprise audit UI, clustering, and
  high availability.
- Browser end-to-end, frontend unit, performance, and penetration tests.

## Requirements for subsequent MVP implementation

### Product and API Contract

1. Treat `PRODUCT_CONTEXT.md` as the authoritative MVP product boundary.
2. Keep OAuth2 client, user, session, token, and error contracts explicit.
3. Exclude passwords, client secrets, tokens, and other credentials from Admin
   API responses and logs. On successful authorization-code exchange,
   `POST /oauth2/token` returns the issued tokens, including the refresh
   token, to the client.
4. Use standards-compatible status codes and validation for all protocol and
   administrative endpoints.

### Backend (`core/`)

1. Protect administrative controllers and OAuth2 authorization actions with
   authenticated administrator/user behavior as appropriate.
2. Implement the minimum client, user, and session behavior needed by the
   scenarios above without introducing unrelated IAM features.
3. Use Spring Authorization Server protocol endpoints and preserve clean
   layering for CredØ-owned domain logic.
4. Add unit tests for new service, mapper, validation, and session logic.
5. Add controller integration tests covering successful and negative paths for
   Admin API and OAuth2 endpoints, including PKCE and token refresh.

### Dependencies and Constraints

- Do not add dependencies without explicit approval.
- Do not add DDL scripts or database migrations.
- Do not change `ENGINEERING_HARNESS.md` to encode CredØ-specific product decisions.
- Resolve open product decisions in `PRODUCT_CONTEXT.md` before dependent
  implementation tasks become `Ready`.

## Acceptance Criteria

- `PRODUCT_CONTEXT.md` contains an explicit MVP goal, journey, scope, and
  acceptance contract linked to this task.
- Required OAuth2/OIDC endpoints, claims, and excluded grants are listed.
- REST-first administration, PostgreSQL, security, operations, deployment, and
  documentation requirements are explicit.
- Full-flow and negative integration tests are required.
- All unresolved decisions are recorded as open decisions rather than hidden
  assumptions.
- `worklog/worklog.md` contains the `0004` entry.

## Assumptions / Open Decisions

1. The exact access-token format, lifetime, refresh-token rotation policy, and
   consent behavior are architecture decisions.
2. PostgreSQL is mandatory for the MVP; the exact schema-versioning mechanism
   must respect the repository's no-migration-script constraint.
3. RSA key storage and rotation procedure remain open for architecture
   planning.
4. The exact administrator authentication and session transport remain open.
5. Admin UI is intentionally deferred until after the REST-first MVP.

## Definition of Done

- MVP goal, journey, boundary, and acceptance criteria are recorded in
  `PRODUCT_CONTEXT.md`.
- This task and its plan are cross-referenced.
- Scope, protocol contracts, security baseline, operations, deployment,
  documentation, test requirements, and open decisions are explicit.
- Worklog is updated with task `0004`.
