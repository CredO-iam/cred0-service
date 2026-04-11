# AI Agent Policy

## Project Structure

The repository has two product layers and changes should be made in the relevant layer(s):

- `admin-ui/` - frontend (Vue 3 + Vite + Tailwind CSS).
  - `admin-ui/src/main.js` and `admin-ui/src/App.vue` - frontend entry points.
  - `admin-ui/src/components` - reusable UI components.
  - `admin-ui/src/shared` - shared frontend utilities/constants.
  - `admin-ui/src/style.css` - global styling with Tailwind CSS v4.
- `core/` - backend layer (Spring Boot).
  - `core/src/main/java/io/cred0/core` - application source code.
  - `core/src/main/java/io/cred0/core/clients` - client domain/API/config/service/persistence.
  - `core/src/main/java/io/cred0/core/security` - backend security configuration.
  - `core/src/main/resources/application.yaml` - backend runtime configuration.

## General

- Follow existing project structure.
- Do not refactor unrelated code.
- Do not introduce breaking changes unless requested.
- All comments must be in English.
- No non-English text inside code.
- If requirements are ambiguous, explicitly state assumptions before implementation.

## Cross-Cutting Implementation Rules

- For every new feature, implement coordinated updates in both frontend and backend when applicable.
- Keep frontend behavior and backend contracts in sync (request/response shape, validation, and error handling).
- If a task adds data fields or business rules, update API, backend domain/service logic, and frontend forms/views together.
- Follow clean layering.
- Do not mix business logic with controllers.
- Avoid framework leakage into domain.

## Planning and Worklog

- For any task that changes code (new features, refactoring, bug fixes, structural updates), create planning artifacts before implementing.
- Two files are mandatory for each code-change task and must share the same number: `./worklog/{NNNN}_TASK_PLAN.md` and `./worklog/{NNNN}_GITHUB_TASK.md`.
- Use continuous numbering across all tasks (no resets) with zero-padded values (`0001`, `0002`, ...).
- `*_TASK_PLAN.md` defines execution steps, risks, and readiness checks; `*_GITHUB_TASK.md` defines full implementation requirements and acceptance contract.
- Use `worklog/0001_TASK_PLAN.md` and `worklog/0001_GITHUB_TASK.md` as the reference level of detail and structure.
- Keep artifacts linked: each plan must reference its paired detailed task file, and each detailed task must reference its paired plan file.
- `*_GITHUB_TASK.md` must include at minimum: `Title`, `Background / Context`, `Goal`, `Scope` (`In Scope`, `Out of Scope`), `Detailed Implementation Requirements` (by affected layer), `Acceptance Criteria`, `Assumptions / Open Decisions`, and `Definition of Done`.
- In `*_GITHUB_TASK.md`, always specify explicit target paths/modules, API/behavior expectations, constraints (including dependency policy), and measurable acceptance criteria.
- Maintain `./worklog/worklog.md` in parallel with a markdown table containing `Number | Date | Short Description`, using ISO date format `YYYY-MM-DD`.
- Add one worklog row per completed task, reusing the same `{NNNN}` as the related plan/task files.

## Frontend Instructions (`admin-ui/`)

- Run frontend commands from `admin-ui/`: `npm install`, `npm run dev`, `npm run build`, `npm run preview`.
- Use the existing `@` alias for imports from `admin-ui/src` as defined in `admin-ui/vite.config.js` and `admin-ui/jsconfig.json`.
- Styling is configured through Tailwind CSS v4 via `@import "tailwindcss";` in `admin-ui/src/style.css` and the `@tailwindcss/vite` plugin.
- When generating frontend UI, pages MUST be container/composition components and MUST NOT keep all markup inline when parts can be extracted.
- Repeated or structurally distinct page parts (for example list rows, form blocks, cards, table rows, dialogs) MUST be moved into reusable child components under `admin-ui/src/components`.
- For list+form screens, split at minimum into: page container + reusable list item row component + reusable create/edit form component.
- Example requirement: a clients screen must be structured like `ClientsPage` (data orchestration) + `ClientRow` (single row rendering/actions) + `ClientForm` (create/edit form rendering/events).
- When the same Tailwind/UI class combinations repeat across components, extract them into shared reusable definitions (for example in `admin-ui/src/shared`) instead of duplicating inline strings.
- There is no router, store, or service layer scaffold checked in yet; add new structure only when the task requires it.
- Treat everything in `admin-ui/src`, `admin-ui/public`, and `admin-ui/index.html` as browser-visible; never hardcode secrets there.
- The current frontend stack in `admin-ui/package.json` is Vue 3 + Vite 7 + Tailwind CSS 4.
- If a dependency change is approved, keep `admin-ui/package-lock.json` in sync.

## Backend Instructions (`core/`)

- In backend code, do not create DDL scripts or database migrations; rely on Hibernate auto table creation.
- When generating `toEntity`/`fromEntity` or `toDto`/`fromDto` mappings, create dedicated utility classes with a private constructor and static methods; do not make them Spring components.
- Validate all external input.
- Never log tokens or passwords.
- Avoid using reflection unless necessary.

## Dependencies

- Do not add new dependencies without explicit request.

## Tests

- All new logic must be covered by unit tests.
- Do not modify existing tests unless broken.
- There is currently no test runner or test configuration checked in under `admin-ui/`; do not assume Vitest, Cypress, or Playwright are already set up.
