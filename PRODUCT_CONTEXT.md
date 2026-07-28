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
- roles and groups where required by the approved MVP model;
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
