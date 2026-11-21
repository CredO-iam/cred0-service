# Plan: Define CredØ MVP Boundaries

Establish the first executable CredØ MVP contract before implementation work
continues. The MVP proves a complete OAuth2/OIDC integration journey,
REST-first administration, production-like persistence, and an extensible
foundation.

[Detailed task](0004_GITHUB_TASK.md) · [MVP contract](../PRODUCT_CONTEXT.md#43-first-mvp-boundary)

## Steps

1. Record the MVP goal and complete developer journey in `PRODUCT_CONTEXT.md`.
2. Define the OAuth2/OIDC protocol, claims, user, client, scope, and role
   boundaries.
3. Define REST-first administration, PostgreSQL persistence, security,
   operations, deployment, and documentation requirements.
4. Define positive and negative integration-test coverage.
5. Record unresolved architecture decisions explicitly rather than inferring
   them in code.
6. Update the paired task and verify all artifacts reference task `0004`.

## Risks

1. An overly broad IAM scope can delay the first usable OAuth2/OIDC flow.
2. Adding Admin UI before the REST contract can expand the MVP unnecessarily.
3. Token and endpoint requirements may be implemented without protocol-level
   integration coverage if they are treated as ordinary CRUD.
4. PostgreSQL and schema-versioning requirements may conflict with the current
   repository constraint against migration scripts.

## Readiness Criteria

1. MVP scope has explicit in-scope and out-of-scope boundaries.
2. The complete developer journey from deployment to UserInfo is an acceptance
   contract.
3. Required OAuth2/OIDC endpoints, claims, and grant exclusions are listed.
4. REST-first administration, PostgreSQL, security baseline, operations,
   deployment, and documentation are explicit.
5. Positive and negative integration-test expectations are explicit.
6. Open decisions are visible and assigned to the next planning step.
