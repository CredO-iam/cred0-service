# Plan: Groups and Roles CRUD Backend and Admin UI

Create a coordinated Groups/Roles management feature across backend and frontend by defining API contracts first, then implementing layered Spring Boot CRUD (`persistence -> service -> controller`) and new Vue pages for operations. This plan is paired with `worklog/0002_GITHUB_TASK.md` and must be used as the implementation baseline.

## Linked Task
- Detailed task definition: `worklog/0002_GITHUB_TASK.md`

## Steps
1. Initialize planning artifacts for task `0002`: create `worklog/0002_TASK_PLAN.md`, create `worklog/0002_GITHUB_TASK.md`, and add a row to `worklog/worklog.md`.
2. Define Groups/Roles API contracts in:
   - `core/src/main/java/io/cred0/core/groups/api`
   - `core/src/main/java/io/cred0/core/roles/api`
   including DTOs and relationship fields.
3. Implement backend persistence in:
   - `core/src/main/java/io/cred0/core/groups/persistence`
   - `core/src/main/java/io/cred0/core/roles/persistence`
   with `GroupEntity`, `RoleEntity`, and JPA repositories.
4. Implement backend service layer in:
   - `core/src/main/java/io/cred0/core/groups/service`
   - `core/src/main/java/io/cred0/core/roles/service`
   for CRUD, relationship synchronization, validation boundaries, and not-found/conflict handling.
5. Expose REST CRUD controllers under `/api/groups` and `/api/roles` with consistent validation and error mapping.
6. Integrate many-to-many relations in backend model and update user-side association handling for:
   - `User <-> Role`
   - `User <-> Group`
   - `Group <-> Role`
7. Implement frontend pages:
   - `admin-ui/src/components/GroupsPage.vue`
   - `admin-ui/src/components/RolesPage.vue`
   and reusable child components (`group` row/form, `role` row/form) for list/create/edit/delete flows.
8. Add backend tests under:
   - `core/src/test/java/io/cred0/core/groups`
   - `core/src/test/java/io/cred0/core/roles`
   covering mapping, service logic, relationship updates, validation, and controller status behavior.

## Relationship Scope
- `User` can have many `Role`; one `Role` can belong to many `User`.
- `User` can belong to many `Group`; one `Group` can contain many `User`.
- `Group` can have many `Role`; one `Role` can belong to many `Group`.

## Risks
1. Relationship write semantics may be ambiguous (replace vs merge) and can cause unintended membership changes.
2. Bidirectional JPA many-to-many mapping may cause recursion/serialization issues if DTO boundaries are not explicit.
3. Frontend/backend drift is likely if relationship payload shape is not fixed early.
4. Uniqueness rules (for example role/group names) may vary by environment unless explicitly validated and conflict-mapped.

## Readiness Criteria
1. `worklog/0002_TASK_PLAN.md` and `worklog/0002_GITHUB_TASK.md` exist and cross-reference each other.
2. Backend exposes CRUD for both `/api/groups` and `/api/roles` with clear relationship handling and stable error responses.
3. Many-to-many links among users, groups, and roles are persisted and returned according to contract.
4. Frontend has working `GroupsPage` and `RolesPage` with reusable list/form components and contract-aligned payloads.
5. New backend logic is covered by tests for happy paths, validation failures, conflict and not-found scenarios.

## Open Questions
1. Relationship update policy for `PUT`: full replacement of linked IDs or patch/merge semantics?
2. Should users management UI include group/role assignment in this task, or only Groups/Roles pages?
3. What uniqueness constraints are required (`group.name`, `role.name`, both case-sensitive or normalized)?
4. Are delete operations hard deletes, and what behavior is required when linked associations exist?

