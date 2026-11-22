# 0009 · Password authentication audit

[Task](0009_GITHUB_TASK.md) · [Plan](0009_TASK_PLAN.md) · Backend implementation

## 2026-11-22 · Starting point and remaining milestones

The [worklog](worklog.md) records tasks 0001–0008. The GitHub repository had no open issues, open PRs or milestones at the start of this task. [Task 0004](0004_GITHUB_TASK.md) defines the first engineering milestone. Users, clients, roles and the bootstrap administrator already have implementations. The checked-in Compose starts a proxy and the backend uses an in-memory database.

| Phase | Next verifiable outcome | Dependencies and owner |
| --- | --- | --- |
| 2 · Architecture | Record decisions on RSA key configuration and rotation, token format and lifetimes, refresh and consent, administrator access, role claims and schema versioning. | Agent prepares ADRs. Operator owns unresolved product choices. |
| 3 · Protocol and administration | Register a client and user, authenticate with PKCE, exchange and refresh tokens, verify the ID token via JWKS and call UserInfo. Cover Client Credentials separately. | Agent implements accepted decisions. Complete scope administration, protect client secrets and enforce administrative access. |
| 3 · Operations | Run the service and PostgreSQL with Compose and retain records across restart. Expose required Actuator signals and audit client creation and token issuance. | Agent implements after storage design and approval for required new dependencies. |
| 4 · System validation | Run the complete developer journey and negative cases for redirect URI, client, expired code, verifier, token and disabled user. | Agent validates the deployed protocol and persistence together. |
| 5 · Release | Reproduce the ten-minute first-token quick start, Admin API guide, Spring Boot and SPA examples. | Agent collects evidence. Operator owns release approval. |

The next proposed protocol task is an ADR for issuer and external RSA key configuration, followed by discovery and public JWKS verification. The combined image with Admin UI and hosted offerings belong to subsequent delivery milestones.

## 2026-11-22 · Implementation

Added a listener for password authentication success and failure and documented its two fixed log messages. The listener uses the existing Spring Security events. The audit record exposes no submitted username, password, exception details or session identifier. This slice covers local authentication. The remainder of the operational and end-to-end MVP contract is tracked above.

The security review found that the default authentication event publisher skips the exception Spring raises for user-store failures. Added its exact exception mapping and a publisher regression test. The tests exercise real form login for success and bad credentials and check one event with a fixed message in both cases.

Validation used Temurin 21 and Gradle 9.4.0. The checkout omits the ignored wrapper JAR, so the official matching wrapper was supplied locally for the run. From core, `./gradlew test` ran 53 tests. All five new tests passed. Two existing tests failed: `GroupEntityMapperTest.toResponseDtoMapsRelationshipIds` and `RoleEntityMapperTest.toResponseDtoMapsRelationshipIds`. Both failed identically in an isolated checkout of origin/main at 3fd098b. The base response mapper returns empty relationship lists. Reconcile the expected read contract before changing the mappers or their tests. The full-suite gate is pending.

A separate run of `./gradlew test --tests '*PasswordAuthenticationAuditTest' --tests '*PasswordLoginAuditIntegrationTest'` passed. Final security review confirmed the mapping and regression coverage. Staged whitespace and local task links passed validation.
