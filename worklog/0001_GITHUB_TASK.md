## Title
Implement User CRUD API and UsersPage refactor (Plan `0001`)

## Background / Context
The repository currently has a clients management flow, but user management is still not implemented end-to-end:
- `admin-ui/src/components/UsersPage.vue` is a placeholder.
- There is no complete backend users module under `core/src/main/java/io/cred0/core/users`.

Plan `worklog/0001_TASK_PLAN.md` defines this as a coordinated backend + frontend delivery and should be used as the execution baseline.

This task must follow `.github/AGENTS.md` rules:
- clean layering (`api -> service -> persistence`),
- no business logic inside controllers,
- dedicated mapper utility classes (private constructor + static methods),
- validate all external input,
- no new dependencies unless explicitly approved,
- split users UI into reusable components,
- keep frontend/backend contract synchronized,
- add unit tests for all new backend logic.

---

## Goal
Deliver production-ready User CRUD functionality across backend and frontend with:
1. a stable and explicit API contract,
2. synchronized request/response models in UI and backend,
3. predictable validation/error handling,
4. backend test coverage for all new logic.

---

## Scope
### In Scope
- Backend users module implementation:
  - entity,
  - repository,
  - service,
  - controller,
  - DTOs and mappers,
  - validation and error mapping.
- Frontend users management implementation:
  - list users,
  - create user,
  - edit user,
  - delete user,
  - reusable `UserRow` and `UserForm` components.
- Contract alignment between backend and frontend for all user fields, including nested objects.

### Domain Fields (must be represented)
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

---

## Detailed Implementation Requirements

## Backend (`core/`)
1. Create/complete module structure under:
   - `core/src/main/java/io/cred0/core/users/api`
   - `core/src/main/java/io/cred0/core/users/service`
   - `core/src/main/java/io/cred0/core/users/persistence`
   - `core/src/main/java/io/cred0/core/users/mappers`

2. Implement persistence:
   - `UserEntity` with required fields.
   - `JpaUserRepository extends JpaRepository<UserEntity, UUID>`.
   - uniqueness behavior for `username` and `email` (enforced + mapped to `409`).

3. Implement service layer:
   - create/read/update/delete operations,
   - timestamp ownership on backend:
     - `POST`: set `createdTimestamp` and `lastModifiedTimestamp`,
     - `PUT`: preserve `createdTimestamp`, refresh `lastModifiedTimestamp`,
   - not-found handling for read/update/delete.

4. Implement API layer:
   - controller endpoints under `/api/users`,
   - request validation at boundary,
   - stable error responses with consistent shape.

5. Mapper rule (mandatory):
   - mapping logic only in dedicated utility classes,
   - utility classes must have private constructor and static methods,
   - no mapper behavior in controllers/entities.

6. Security/logging constraints:
   - never log secrets/passwords/token-like fields,
   - do not expose raw credential values in responses.

---

## Frontend (`admin-ui/`)
1. Refactor `admin-ui/src/components/UsersPage.vue` into container/orchestration component.
2. Create reusable components:
   - `admin-ui/src/components/users/UserRow.vue`
   - `admin-ui/src/components/users/UserForm.vue`
3. Implement CRUD UX:
   - load users on page mount,
   - refresh list action,
   - create/edit form flow,
   - delete action with per-row state,
   - success/error feedback handling.
4. Keep payload model identical to backend contract:
   - same property names,
   - same nested structure for `attributes` and `credentials`.
5. Use essential client-side validation (required fields + email format), but backend remains source of truth.
6. Do not add dependencies.

---

## API Contract

Base path: `/api/users`

### Endpoints
- `GET /api/users` -> `200 OK`
- `GET /api/users/{id}` -> `200 OK`, `404 Not Found`, `400 Bad Request` for malformed UUID
- `POST /api/users` -> `201 Created`, `400 Bad Request`, `409 Conflict`
- `PUT /api/users/{id}` -> `200 OK`, `400 Bad Request`, `404 Not Found`, `409 Conflict`
- `DELETE /api/users/{id}` -> `204 No Content`, `404 Not Found`, `400 Bad Request` for malformed UUID

### Example Request (Create)
```json
{
  "username": "jdoe",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "enabled": true,
  "emailVerified": false,
  "attributes": [
    { "name": "department", "value": "engineering" }
  ],
  "credentials": {
    "type": "password",
    "value": "temporary-secret"
  }
}
```

### Example Response (User)
```json
{
  "id": "8f0f2f7d-2a9b-48ca-bf5e-53d92d82c6fe",
  "username": "jdoe",
  "firstName": "John",
  "createdTimestamp": 1775400000000,
  "lastModifiedTimestamp": 1775400000000,
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "enabled": true,
  "emailVerified": false,
  "attributes": [
    { "name": "department", "value": "engineering" }
  ],
  "credentials": {
    "type": "password",
    "secretSet": true
  }
}
```

### Error Response Shape (expected)
```json
{
  "timestamp": "2026-04-06T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "email must be a valid email address",
  "path": "/api/users"
}
```

Error handling requirements:
- validation failures -> `400` with actionable `message`,
- missing entity -> `404`,
- uniqueness conflicts (`username`/`email`) -> `409`,
- unknown server errors -> `500` with safe generic message.

---

## Acceptance Criteria
- Backend exposes complete User CRUD under `/api/users` according to contract.
- Backend layering is clean and policy-compliant.
- Mapping is implemented through dedicated utility mapper classes only.
- Input validation and status codes are consistent and predictable.
- `UsersPage` is implemented as container + reusable `UserRow` and `UserForm`.
- Frontend/backend models are synchronized for all fields including nested structures.
- No new dependencies introduced.
- All new backend logic is covered by unit tests and passing.

---

## Test Strategy
Add tests under `core/src/test/java/io/cred0/core/users`:
1. Mapper tests:
   - DTO <-> entity conversions for all fields, including nested `attributes` and `credentials`.
2. Service tests:
   - create/update timestamp behavior,
   - not-found behavior,
   - uniqueness conflict behavior,
   - validation/normalization logic.
3. Controller/API tests:
   - CRUD happy paths,
   - malformed UUID -> `400`,
   - validation failures -> `400`,
   - missing entity -> `404`,
   - uniqueness conflict -> `409`,
   - error payload shape assertions.

If frontend automated tests are not configured, document the gap in PR notes without introducing new testing dependencies.

---

## Out of Scope
- authentication/authorization redesign,
- pagination/sorting/filtering,
- bulk user operations,
- migration tooling/DDL scripts,
- unrelated refactors in other domains,
- dependency upgrades/additions.

---

## Assumptions / Open Decisions
1. `POST /api/users` ID policy: backend generates UUID (client-provided ID is ignored or rejected consistently).
2. `credentials` response policy: do not return raw secret values; return redacted/metadata representation.
3. `attributes` persistence approach (normalized table vs JSON) should be chosen explicitly and documented before implementation.

---

## Definition of Done
- Users module implemented in backend with API/service/persistence/mappers layers.
- Users CRUD UI implemented with reusable components and contract-aligned payloads.
- Backend tests for all new users logic are implemented and passing.
- Implementation follows `worklog/0001_TASK_PLAN.md` and `.github/AGENTS.md` requirements.

