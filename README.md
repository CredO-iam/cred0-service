# CredØ

CredØ is an open-source identity and access management service for applications that use OAuth2 and OpenID Connect. It combines a Spring backend with a web interface for managing users, roles and registered applications. The source is available under the [MIT license](LICENSE).

## Planned distribution

| Offering | How it will work |
| --- | --- |
| Self-hosted | Run one Docker image containing the service and its admin UI, ready to use with documented defaults. |
| Free SaaS | Create and manage your own OAuth2 clients in a shared hosted environment. |
| Paid SaaS | Provision an isolated instance of the same service. Open its admin UI from the common SaaS admin portal. |

Each instance represents one security domain with its own issuer. The default distribution focuses on OAuth2 and OpenID Connect. Additional modules may provide other protocols, including SAML. The [product context](PRODUCT_CONTEXT.md) defines the delivery vision and the first MVP acceptance criteria.

## Current checkout

The Java 21 backend in [core](core/) uses Spring Boot, Spring Security and JPA. It exposes REST endpoints under `/admin/users`, `/admin/groups`, `/admin/roles` and `/admin/registered-clients`. The Vue 3 frontend in [admin-ui](admin-ui/) provides login and management screens for these resources. At startup the backend creates an initial administrator, the admins group and the system_admin role. Administrative routes currently require an authenticated session.

Local development uses H2 in memory and recreates the schema at startup. The first MVP requires PostgreSQL persistence and an end-to-end Authorization Code flow with PKCE, ID token signature verification and UserInfo. The combined application image and SaaS offerings are planned deliverables.

## Run locally

1. In `core/`, run `./gradlew bootRun` with Java 21. The bootstrap account is configured with `CRED0_BOOTSTRAP_ADMIN_USERNAME` and `CRED0_BOOTSTRAP_ADMIN_PASSWORD`. Local defaults appear in the [application configuration](core/src/main/resources/application.yaml).
2. In `admin-ui/`, run `npm install` and then `npm run dev`.
3. Open `http://localhost:5173`. Vite forwards admin and login requests to the backend on port 8080.

For the optional local Nginx proxy, run `docker compose up` from the repository root while the backend and frontend are running. Open `http://localhost:8088`. The current [Compose file](docker-compose.yml) starts the proxy only.

## Contributor references

- [Registered-client API specification](core/doc/SPEC.md)
- [Engineering process](ENGINEERING_HARNESS.md)
- [Task workflow](worklog/WORKFLOW.md) and [completed work](worklog/worklog.md)
- [Lessons learned from operator corrections](.agents/skills/lessons-learned/SKILL.md)
