# Plan: User CRUD Backend and Users UI

Deliver a full user-management foundation by defining a stable backend API contract first, then implementing layered Spring Boot CRUD (`persistence` -> `service` -> `controller`) and aligning the Vue users screen with reusable components and identical validation rules. This keeps frontend/backend behavior synchronized, reduces contract drift, and ensures the task is traceable under the Planning and Worklog policy as plan `0001`.

## Steps
1. Initialize planning artifacts: create `worklog/0001_TASK_PLAN.md` and add `0001 | 2026-04-06 | User CRUD backend and UsersPage` to `worklog/worklog.md`.
2. Define the User API contract in `core/src/main/java/io/cred0/core/users/api` with `UserDto`, nested attribute/credentials DTOs, and CRUD payload/response conventions.
3. Implement backend persistence in `core/src/main/java/io/cred0/core/users/persistence` with `UserEntity`, related value structures, and `JpaUserRepository` (`JpaRepository<UserEntity, UUID>`).
4. Implement business layer in `core/src/main/java/io/cred0/core/users/service` with `UserService` for create/read/update/delete, timestamp handling, and domain validation boundaries.
5. Expose REST CRUD in `core/src/main/java/io/cred0/core/users/api/UserController.java` (`GET /api/users`, `GET /api/users/{id}`, `POST`, `PUT`, `DELETE`) with consistent error mapping and input validation.
6. Refactor `admin-ui/src/components/UsersPage.vue` into container + reusable parts (`admin-ui/src/components/users/UserRow.vue`, `admin-ui/src/components/users/UserForm.vue`), wire API calls, and mirror backend request/response fields exactly.
7. Add backend tests under `core/src/test/java/io/cred0/core/users` for repository mappings, service validation/business rules, and controller CRUD/error cases; document any frontend test gap if no runner is configured.

## User Object Scope
- `UUID id`
- `String username`
- `String firstName`
- `Long createdTimestamp`
- `Long lastModifiedTimestamp`
- `String lastName`
- `String email`
- `boolean enabled`
- `boolean emailVerified`
- `Collection<UserAttribute> attributes`
- `Credentials credentials`

## Risks
1. Contract drift risk between `UserDto` and UI form model; mitigate with one explicit field-mapping checklist and fixed JSON examples in the plan.
2. Complex nested fields (`attributes`, `credentials`) may cause serialization/persistence mismatches; mitigate by defining canonical shapes and null/empty handling upfront.
3. Validation inconsistency (frontend permissive vs backend strict) can break UX; mitigate by duplicating only essential rules in UI and treating backend as source of truth.

## Readiness Criteria
1. `worklog/0001_TASK_PLAN.md` and `worklog/worklog.md` exist and use policy-compliant numbering/date format.
2. Backend provides complete User CRUD with UUID id, required timestamps, validation, and predictable error responses.
3. Frontend `UsersPage` supports list/create/edit/delete flows and uses reusable `UserRow`/`UserForm` components.
4. Frontend/backend contracts are explicitly synchronized for all User fields, including nested `attributes` and `credentials`.
5. New backend logic is covered by tests for happy paths, validation failures, not-found, and update/delete behavior.

## Open Questions
1. Should `POST` accept client-provided `id` or always generate server-side UUID?
2. Should `credentials` be fully returned in read responses or redacted/metadata-only?
3. For `attributes`, should JPA use normalized child entities or JSON persistence?

