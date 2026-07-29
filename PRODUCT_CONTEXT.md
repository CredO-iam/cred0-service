# Product Context: CredØ

## 1. Purpose

This document contains product-specific context for CredØ. It is used by the
AI-driven engineering harness described in [`CONCEPT.md`](CONCEPT.md).

`CONCEPT.md` defines how engineering work is organized. This document defines
what product is being built, why it is being built, and which product
constraints are currently known.

Product context is authoritative for product intent, scope, and behavior.
Technical implementation details remain subject to the planning and
architecture phases unless explicitly confirmed here.

## 2. Product Vision

CredØ is a focused and extensible identity and authorization service for
modern OAuth2 and OpenID Connect use cases.

The product should provide the capabilities needed to manage identities,
clients, and access rules, and to issue standards-based authorization
artifacts. It should remain:

- simple to use and configure;
- easy to understand and operate;
- familiar to Java and Spring developers;
- focused on coherent core capabilities;
- extensible without requiring a proprietary platform.

Its product direction is:

> Simple by default. Powerful when needed.

## 3. Intended Users

The primary audience is developers and teams that need an OAuth2/OIDC
authorization service and prefer a focused Spring-native solution over a large
IAM platform.

The product should be understandable to developers who already know Spring
Boot, Spring Security, and standard OAuth2/OIDC concepts.

## 4. Confirmed Scope Decisions

### 4.1. In Scope

- OAuth2;
- OpenID Connect;
- identity and user management required by the MVP;
- clients and client administration required by the MVP;
- roles and groups where required by an approved product milestone;
- an administrative REST API;
- an administrative UI;
- Spring-native extension points.

The exact MVP capability set is to be discovered and confirmed during
Phase 1 of the engineering harness.

### 4.2. Explicitly Out of Scope

- SAML;
- realm-based multi-tenancy;
- built-in multi-tenancy;
- a universal proprietary plugin or SPI ecosystem;

### 4.3. First MVP Boundary

The first CredØ MVP exists to prove that:

- standards-based OAuth2 and OpenID Connect protocols work;
- application integration is straightforward;
- basic identity and client administration is possible; and
- the system has a coherent foundation for later extension.

The MVP is complete only when the following developer journey works:

1. Deploy the server with `docker compose up`.
2. Create a user and an OAuth2 client through the Admin API.
3. Configure a registered redirect URI.
4. Start a standard Authorization Code with PKCE flow.
5. Let the user authenticate and authorize the application.
6. Exchange the authorization code for access, refresh, and ID tokens.
7. Validate the ID token signature using the JWKS endpoint.
8. Call UserInfo with the access token.

#### OAuth2 and OIDC capabilities

Must-have OAuth2 capabilities:

- authorization endpoint and token endpoint;
- redirect URI validation;
- PKCE challenge/verifier validation;
- authorization-code lifecycle and expiration;
- access-token issuance;
- refresh-token issuance and refresh;
- Client Credentials for machine-to-machine integrations.

Must-have OIDC capabilities:

- `/.well-known/openid-configuration`;
- `/oauth2/jwks`;
- ID Tokens with `iss`, `sub`, `aud`, `exp`, `iat`, and `nonce` when used;
- `UserInfo` with at least `sub`, `email`, and `name`.

Device Authorization Grant, Token Exchange, CIBA, JWT Bearer Grant, implicit
grant, and Resource Owner Password Credentials are outside this MVP.

The protocol surface must include:

- `GET /oauth2/authorize`;
- `POST /oauth2/token`;
- `GET /oauth2/jwks`;
- `GET /.well-known/openid-configuration`;
- `GET /userinfo`.

#### Identity and claims model

The MVP identity model contains users with an identifier, username/email,
password hash, enabled state, and extensible attributes. Clients contain a
client identifier, protected client secret where applicable, redirect URIs,
grant types, scopes, and enabled state.

The MVP includes named scopes such as `openid`, `profile`, and `email`, and
roles only. It does not include groups, permission hierarchies, fine-grained
authorization, or a policy engine. The initial claim contract is:

- ID Token: `sub`, `iss`, `aud`, `exp`, `iat`, `email`, `name`, and `nonce`
  when applicable;
- access token: `sub`, `scope`, and roles;
- UserInfo: `sub`, `email`, and `name`.

#### Administration, persistence, and operations

The MVP is REST-first: the Admin API must support user, client, scope, and
role administration. Admin UI is a follow-up milestone and is not required
to prove the first MVP journey. Swagger/OpenAPI documentation may be used as
the operator-facing interface for the Admin API.

PostgreSQL is the required MVP persistence target; H2 may remain available for
demo or local test use but is not sufficient as the only MVP database.
Schema versioning and migration strategy must be resolved during
architecture planning without violating the repository rule against
committing DDL or migration scripts.

Passwords must use BCrypt or Argon2id. Secrets and signing keys must come from
environment variables or external configuration and must never be logged.
The MVP uses an RSA signing key pair and publishes its public keys through
JWKS. Key rotation is desirable but may be limited to a documented operational
procedure.

Spring Actuator health, info, and metrics endpoints are required. The system
must record a basic audit event model covering at least successful and failed
authentication, client creation, and token issuance.

#### Testing, deployment, and documentation

The MVP requires unit tests plus automated integration coverage for the full
journey: client creation, authorization request, login, code issuance, token
exchange, JWT validation, and UserInfo. Negative coverage must include invalid
redirect URI, invalid client, expired code, invalid PKCE verifier, invalid
token, and disabled user.

The deliverable must include an application container, PostgreSQL,
configuration example, database setup/versioning approach, and a developer
quick start that obtains the first token in ten minutes. An Admin API guide
and integration examples for a Spring Boot application and a frontend SPA are
also required.

#### MVP non-goals

SAML, multi-tenancy, LDAP, federation, social login, MFA, passwordless
authentication, fine-grained authorization, custom policies, workflow engines,
plugin marketplaces, enterprise audit UI, clustering, and high availability
are explicitly outside the MVP.

#### MVP acceptance criteria

The MVP is accepted when a developer can deploy the server locally, create a
user and client through the Admin API, complete Authorization Code with PKCE,
receive standards-compatible OAuth2/OIDC tokens, verify the ID Token using
JWKS, call UserInfo, observe data surviving restart, and run the automated
integration suite and documented deployment successfully.

## 5. Deployment and Security-Domain Model

One running CredØ instance represents one independent security domain. An
instance has one user base, one set of clients, one set of roles and related
security data, and one issuer/security context.

Independent security domains should be deployed as separate instances rather
than represented as realms inside one instance.

The product should not introduce realm management or multi-tenant complexity
unless this decision is explicitly revisited and recorded.

## 6. Product Principles

These principles guide product decisions and are distinct from the universal
engineering principles in `CONCEPT.md`.

### 6.1. Convention Over Configuration

Common use cases should work with minimal configuration and sensible defaults.

### 6.2. Spring-Native

The architecture and extension model should feel natural to Java and Spring
developers and should avoid unnecessary proprietary abstractions.

### 6.3. REST-First Administration

Capabilities exposed through the Admin UI should, where appropriate, also be
available through a first-class REST API. The API must not be treated merely
as an internal UI implementation detail.

### 6.4. Predictable Behavior

Configuration and runtime behavior should be understandable, documented, and
free of unnecessary magic.

### 6.5. Focused Configuration

The product should prefer a small number of clear configuration options over a
large set of obscure settings.

### 6.6. Spring-Native Extensibility

Customization should use familiar Spring mechanisms wherever practical,
including beans, interfaces, dependency injection, bean replacement,
`@Primary`, Spring Security extension points, Spring Authorization Server
extension points, custom authentication providers, token customizers, user
services, handlers, and REST endpoints.

The public extension surface must be defined during architecture design.

## 7. Initial Technology Direction

The current technology direction is:

- backend: Java and/or Kotlin;
- framework: Spring Boot;
- authorization server: Spring Authorization Server;
- frontend: Vue 3;
- primary protocols: OAuth2 and OpenID Connect.

This is an initial direction, not a complete architecture decision. Any
material change must be evaluated through the engineering harness and recorded
as a decision.

## 8. Current Repository Context

The repository currently contains two product layers:

- `core`: Spring Boot backend and authorization core;
- `admin-ui`: Vue 3 administrative frontend.

The current product areas include users, groups, roles, registered OAuth2
clients, security configuration, and administrative screens. Existing
implementation is evidence of current product state, not a substitute for
approved requirements.

## 9. Product Lifecycle

The product is expected to progress through:

1. Phase 0 — Engineering Harness;
2. Phase 1 — Product/MVP Definition and Planning;
3. Phase 2 — Architecture and Technical Design;
4. Phase 3 — MVP Implementation;
5. Phase 4 — Integration and System Validation;
6. Phase 5 — MVP Release;
7. Phase 6 — Post-MVP and subsequent milestones.

The immediate product task after the harness is ready is Phase 1. The next
step is not to assume a complete IAM domain model, but to progressively
discover the MVP requirements and unresolved product decisions.

## 10. Open Product Questions

The following questions are intentionally unresolved and must be handled
progressively by the harness:

- Which OAuth2 flows are required for the MVP?
- Which OIDC capabilities are required?
- What user, client, role, group, scope, permission, and authority model is
  required?
- How are authentication, passwords, tokens, and claims managed?
- Which Admin API and Admin UI capabilities are required?
- What persistence, deployment, security, testing, and documentation
  requirements apply?
- Which extension points are public and supported?

The first MVP resolves the flow, OIDC, identity, persistence, administration,
operations, and acceptance boundary as documented in section 4.3. The
following decisions remain open for architecture planning:

- access-token format and token lifetimes;
- refresh-token rotation and reuse detection;
- consent behavior;
- exact PostgreSQL schema-versioning mechanism;
- administrator authentication and session transport;
- RSA key storage and rotation procedure;
- role assignment and claim naming details;
- the post-MVP Admin UI milestone.

AI must not silently convert these questions into product requirements.

## 11. Relationship With the Engineering Harness

The relationship between the two documents is normative:

1. Read `CONCEPT.md` to determine the engineering process.
2. Read `PRODUCT_CONTEXT.md` to determine CredØ product intent and constraints.
3. During each phase, derive work from both documents.
4. Product decisions must not be added to the generic harness merely because
   they are true for CredØ.
5. Generic improvements to the engineering process must not change CredØ
   product scope without an explicit product decision.
6. Decisions discovered during execution must identify whether they update the
   harness, this product context, architecture, or an implementation task.

For another product, the same harness can be reused with a different
Product Context document.

## 12. Initial Product Brief

Create a focused identity and authorization service for OAuth2 and OpenID
Connect. Provide clear administration of identities, clients, and access
rules, while keeping the product simple to use, configure, understand, and
operate for Java/Spring developers. Use Spring Boot and Spring Authorization
Server for the backend direction and Vue 3 for the frontend direction. Exclude
SAML and realm-based multi-tenancy. Prefer separate instances for separate
security domains and use Spring-native extension mechanisms instead of a
custom universal plugin platform.
