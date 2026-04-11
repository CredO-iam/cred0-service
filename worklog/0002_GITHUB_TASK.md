## Title
Implement Groups and Roles CRUD API and Admin UI (Plan `0002`)

## Background / Context
The project needs first-class authorization domain management beyond users: groups and roles are not implemented as complete CRUD modules yet.

This task introduces coordinated backend and frontend support for:
- `Group` and `Role` entities with CRUD operations,
- many-to-many relationships between users, groups, and roles,
- dedicated admin pages for operational management.

Execution must follow `worklog/0002_TASK_PLAN.md` and `.github/AGENTS.md` constraints:
- clean layering (`api -> service -> persistence`),
- no business logic in controllers,
- mapper utility classes (private constructor + static methods),
- validate all external input,
- keep frontend/backend contracts synchronized,
- no new dependencies unless explicitly approved,
- unit tests for all new backend logic.

---

## Linked Plan
- Implementation plan: `worklog/0002_TASK_PLAN.md`

---

## Goal
Deliver production-ready Groups/Roles management across backend and frontend with explicit relationship handling:
1. complete CRUD APIs for groups and roles,
2. deterministic many-to-many link management (`User <-> Role`, `User <-> Group`, `Group <-> Role`),
3. frontend pages for listing/creating/updating/deleting groups and roles,
4. robust validation, conflict handling, and backend test coverage.

---

## Scope
### In Scope
- Backend modules for groups and roles (`entity`, `repository`, `service`, `controller`, `dto`, `mapper`).
- CRUD endpoints under `/api/groups` and `/api/roles`.
- Relationship mapping and update semantics for:
  - users-groups,
  - users-roles,
  - groups-roles.
- Frontend pages and reusable components for Groups and Roles management.
- Validation/error behavior aligned between frontend and backend.

### Out of Scope
- Authentication/authorization redesign.
- Fine-grained permission engine.
- Pagination/sorting/filtering and bulk operations.
- DB migrations/DDL scripts.
- New dependencies.
- Unrelated domain refactoring.

---

## Detailed Implementation Requirements

## Backend (`core/`)
1. Create/complete module structure:
   - `core/src/main/java/io/cred0/core/groups/api`
   - `core/src/main/java/io/cred0/core/groups/service`
   - `core/src/main/java/io/cred0/core/groups/persistence`
   - `core/src/main/java/io/cred0/core/groups/mappers`
   - `core/src/main/java/io/cred0/core/roles/api`
   - `core/src/main/java/io/cred0/core/roles/service`
   - `core/src/main/java/io/cred0/core/roles/persistence`
   - `core/src/main/java/io/cred0/core/roles/mappers`

2. Implement persistence:
   - `GroupEntity` and `RoleEntity` with UUID identifiers.
   - `JpaGroupRepository extends JpaRepository<GroupEntity, UUID>`.
   - `JpaRoleRepository extends JpaRepository<RoleEntity, UUID>`.
   - many-to-many entity mappings for:
     - users <-> groups,
     - users <-> roles,
     - groups <-> roles.
   - explicit uniqueness constraints and lookups for group/role identifiers (for example name/code) with conflict mapping to HTTP `409`.

3. Implement service layer:
   - CRUD create/read/update/delete for groups and roles.
   - Relationship synchronization by ID collections in write operations.
   - Consistent not-found handling for related entities and targets.
   - Clear update behavior (replace semantics unless otherwise specified).

4. Implement API layer:
   - controllers for `/api/groups` and `/api/roles` with CRUD endpoints.
   - request validation at boundary (`@Valid`, required fields, ID format).
   - stable error response shape and status code mapping.

5. Mapper rule (mandatory):
   - all DTO/entity transformations implemented in utility mapper classes,
   - private constructor + static methods,
   - no mapping logic inside controllers/entities.

6. Constraints:
   - do not add dependencies,
   - do not generate DDL/migration scripts,
   - never log secrets/sensitive payload values.

---

## Frontend (`admin-ui/`)
1. Create page-level container components:
   - `admin-ui/src/components/GroupsPage.vue`
   - `admin-ui/src/components/RolesPage.vue`

2. Create reusable page parts:
   - `admin-ui/src/components/groups/GroupRow.vue`
   - `admin-ui/src/components/groups/GroupForm.vue`
   - `admin-ui/src/components/roles/RoleRow.vue`
   - `admin-ui/src/components/roles/RoleForm.vue`

3. Implement CRUD UX for both pages:
   - load list on mount,
   - create/edit flows,
   - delete action,
   - in-form relation assignment by IDs (users, roles, groups as applicable),
   - success/error feedback.

4. Keep payload model aligned with backend contract:
   - same field names,
   - relationship fields represented consistently (for example `userIds`, `roleIds`, `groupIds`).

5. Preserve existing architecture constraints:
   - no router/store assumptions unless task explicitly requires,
   - extract repeated UI class combinations into shared constants when repeated,
   - no dependency additions.

---

## API Contract Expectations
Base paths:
- `/api/groups`
- `/api/roles`

Required endpoints for each resource:
- `GET /api/{resource}` -> `200 OK`
- `GET /api/{resource}/{id}` -> `200 OK`, `404 Not Found`, `400 Bad Request` (malformed UUID)
- `POST /api/{resource}` -> `201 Created`, `400 Bad Request`, `409 Conflict`
- `PUT /api/{resource}/{id}` -> `200 OK`, `400 Bad Request`, `404 Not Found`, `409 Conflict`
- `DELETE /api/{resource}/{id}` -> `204 No Content`, `404 Not Found`, `400 Bad Request`

Relationship payload expectation (write model):
- Group write DTO includes role and user links by IDs.
- Role write DTO includes group and user links by IDs.

Error handling:
- validation failures -> `400`,
- missing target/linked entity -> `404`,
- uniqueness conflict -> `409`,
- unexpected errors -> `500` with safe generic message.

---

## Acceptance Criteria
- Backend provides full CRUD under `/api/groups` and `/api/roles`.
- Many-to-many relations (`User <-> Role`, `User <-> Group`, `Group <-> Role`) are persisted and exposed according to contract.
- Controllers remain thin; business logic is in services; mapping is in dedicated mapper utilities only.
- Input validation and error/status behavior are deterministic and consistent.
- Frontend includes working `GroupsPage` and `RolesPage` with reusable row/form components.
- Frontend/backend contracts are synchronized for base fields and relationship fields.
- No new dependencies are added.
- New backend logic is covered by unit tests and passing.

---

## Test Strategy
Add tests under:
- `core/src/test/java/io/cred0/core/groups`
- `core/src/test/java/io/cred0/core/roles`

Minimum coverage:
1. Mapper tests:
   - DTO <-> entity field mappings,
   - relationship ID mapping behavior.
2. Service tests:
   - CRUD happy paths,
   - not-found behavior,
   - conflict behavior for unique fields,
   - relationship synchronization semantics.
3. Controller/API tests:
   - CRUD status codes,
   - malformed UUID handling,
   - validation failures,
   - relationship target not-found,
   - error payload shape assertions.

If frontend test tooling is not configured, document manual verification scenarios in PR notes without adding new tooling.

---

## Assumptions / Open Decisions
1. Group and role unique business keys are required and mapped to `409` on conflict.
2. Relationship updates in `PUT` use replacement semantics for linked ID sets unless changed explicitly.
3. Deleting a group/role detaches many-to-many links safely before removal (no orphaned relation rows).
4. Users UI extension for assigning groups/roles is deferred unless explicitly required by implementation review.

---

## Definition of Done
- `Group` and `Role` modules implemented with `api/service/persistence/mappers` layers.
- CRUD endpoints and relationship management implemented and validated.
- `GroupsPage` and `RolesPage` implemented with reusable row/form components.
- Backend tests for all new groups/roles logic are implemented and passing.
- Implementation follows `worklog/0002_TASK_PLAN.md` and `.github/AGENTS.md`.

