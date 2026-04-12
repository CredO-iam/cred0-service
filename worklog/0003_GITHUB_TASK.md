## Title
Bootstrap admin user/group/role on backend startup (Plan `0003`)

## Background / Context
Backend currently allows CRUD for users, groups, and roles, but startup does not guarantee presence of a baseline administrative model.

Plan `worklog/0003_TASK_PLAN.md` defines this delivery and serves as implementation baseline.

This task follows `.github/AGENTS.md` constraints:
- clean layering,
- input validation and safe defaults,
- no new dependencies,
- tests for all new logic,
- no secrets in logs.

---

## Goal
Initialize core authorization entities automatically on backend startup:
1. ensure user `admin` exists,
2. ensure group `admins` exists,
3. ensure role `system_admin` exists,
4. ensure relations `admin -> admins` and `admins -> system_admin` exist,
5. source admin login/password from env with default fallback values via `@Value`.

---

## Scope
### In Scope
- Backend-only bootstrap implementation in `core/`.
- Runtime configuration for env-based admin `username/password` only.
- Idempotent startup behavior and relation reconciliation.
- Unit tests for bootstrap logic.

### Out of Scope
- Frontend changes.
- Authentication flow redesign.
- Password hashing redesign.
- Additional roles/groups beyond requested defaults.
- Dependency changes.
- Introducing `AdminBootstrapProperties` (`@ConfigurationProperties`) now; it will be added later together with other bootstrap variables.

---

## Detailed Implementation Requirements

## Backend (`core/`)
1. Add bootstrap configuration keys in `core/src/main/resources/application.yaml`:
   - `cred0.bootstrap.admin.username` <- `${CRED0_BOOTSTRAP_ADMIN_USERNAME:admin}`
   - `cred0.bootstrap.admin.password` <- `${CRED0_BOOTSTRAP_ADMIN_PASSWORD:change_me_admin_password}`
   - no additional bootstrap keys are required in this task.

2. In startup initializer, bind only admin login/password through `@Value`:
   - `@Value("${cred0.bootstrap.admin.username:admin}")`
   - `@Value("${cred0.bootstrap.admin.password:change_me_admin_password}")`
   - do not add `AdminBootstrapProperties` in this task.

3. Add startup initializer:
   - `core/src/main/java/io/cred0/core/bootstrap/AdminBootstrapInitializer.java`
   - executes on startup (`ApplicationRunner`), transactional, idempotent.
   - creates missing entities with timestamps/UUIDs and required user fields.
   - never logs admin password.

4. Repository support:
   - extend `core/src/main/java/io/cred0/core/users/persistence/JpaUserEntityRepository.java`
   - add `Optional<UserEntity> findByUsernameIgnoreCase(String username)`.

5. Entity defaults for bootstrap user:
   - username from env/default,
   - credentials value from env/default password,
   - credentials type uses current backend default (no new config key in this task),
   - `firstName = "Admin"`, `lastName = "User"`,
   - `attributes = "[]"`,
   - `enabled = true`,
   - `emailVerified = false`,
   - `email` uses current backend default/static value.

6. Relation consistency:
   - user must be linked to group,
   - group must be linked to role,
   - preserve existing entities and only add missing links.

---

## Acceptance Criteria
- Startup creates missing `admin`, `admins`, `system_admin` when absent.
- Startup keeps behavior idempotent across restarts.
- Required links exist after startup.
- Admin username/password are read via `@Value` from env-backed properties with fallback defaults.
- No passwords/tokens are logged.
- No new dependencies.
- `AdminBootstrapProperties` is not introduced in this task.
- Unit tests for new bootstrap logic are present and passing.

---

## Assumptions / Open Decisions
1. Existing admin user is not overwritten if already present.
2. Blank env values fallback to defaults.
3. Default bootstrap password is acceptable for local/dev startup and must be overridden in production.

---

## Definition of Done
- Files from this task are implemented in backend and covered with tests.
- `worklog/0003_TASK_PLAN.md` and `worklog/0003_GITHUB_TASK.md` remain synchronized.
- `worklog/worklog.md` contains an entry for `0003`.

