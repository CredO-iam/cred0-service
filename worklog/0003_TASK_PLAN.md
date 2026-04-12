# Plan: Backend Startup Admin Bootstrap

Implement idempotent backend startup initialization that guarantees a default administrative access model exists: user `admin`, group `admins`, role `system_admin`, and links `admin -> admins -> system_admin`. Login/password come from environment variables with safe defaults.

Paired detailed task: `worklog/0003_GITHUB_TASK.md`.

## Steps
1. Create planning artifacts for task `0003` and update `worklog/worklog.md` according to policy.
2. Add bootstrap configuration properties in backend runtime config (`core/src/main/resources/application.yaml`) for admin login/password sourced from env with defaults.
3. Extend user repository lookup capabilities with `findByUsernameIgnoreCase` in `core/src/main/java/io/cred0/core/users/persistence/JpaUserEntityRepository.java`.
4. Implement bootstrap config holder in `core/src/main/java/io/cred0/core/bootstrap/AdminBootstrapProperties.java`.
5. Implement startup initializer in `core/src/main/java/io/cred0/core/bootstrap/AdminBootstrapInitializer.java` that:
   - creates missing role/group/user,
   - links user to group,
   - links group to role,
   - is idempotent across restarts.
6. Add unit tests in `core/src/test/java/io/cred0/core/bootstrap/AdminBootstrapInitializerTest.java` covering empty database bootstrap, idempotent restart, and missing-link reconciliation.
7. Run backend tests and verify no regressions.

## Risks
1. Non-idempotent bootstrap can create duplicate links or trigger unique constraint violations.
2. Incorrect many-to-many synchronization may save only one side and lose relations.
3. Empty/invalid env-provided credentials can create unusable bootstrap user.

## Readiness Criteria
1. `worklog/0003_TASK_PLAN.md` and `worklog/0003_GITHUB_TASK.md` are present and cross-referenced.
2. On a clean start, backend contains user `admin`, group `admins`, role `system_admin`.
3. Relations are present: user is in group, group has role.
4. Re-running startup does not create duplicates and does not rewrite unchanged entities.
5. New bootstrap logic has unit tests and they pass.

## Open Questions
1. Should existing admin user password be rotated on every startup if env value changes? (assumed: no)
2. Should bootstrap fail startup on invalid blank credentials or fallback silently? (assumed: fallback to defaults when blank)

