# AI Agent Policy

## General

- Follow existing project structure.
- Do not refactor unrelated code.
- Do not introduce breaking changes unless requested.
- This repository currently contains a single frontend app in `admin-ui/`; make product code changes there unless the task explicitly targets repo metadata.
- Run frontend commands from `admin-ui/`: `npm install`, `npm run dev`, `npm run build`, `npm run preview`.

## Language

- All comments must be in English.
- No non-English text inside code.

## Architecture

- Follow clean layering.
- Do not mix business logic with controllers.
- Avoid framework leakage into domain.
- The current frontend entry points are `admin-ui/src/main.js` and `admin-ui/src/App.vue`.
- When generating frontend UI, always split pages into dedicated Vue components (use `admin-ui/src/components` and keep `App.vue` as composition/root shell where possible).
- When the same Tailwind/UI class combinations repeat across components, extract them into shared reusable definitions (for example in `admin-ui/src/shared`) instead of duplicating inline strings.
- Use the existing `@` alias for imports from `admin-ui/src` as defined in `admin-ui/vite.config.js` and `admin-ui/jsconfig.json`.
- Styling is configured through Tailwind CSS v4 via `@import "tailwindcss";` in `admin-ui/src/style.css` and the `@tailwindcss/vite` plugin.
- There is no router, store, or service layer scaffold checked in yet; add new structure only when the task requires it.

## Dependencies

- Do not add new dependencies without explicit request.
- The current frontend stack in `admin-ui/package.json` is Vue 3 + Vite 7 + Tailwind CSS 4.
- If a dependency change is approved, keep `admin-ui/package-lock.json` in sync.

## Tests

- All new logic must be covered by tests.
- Do not modify existing tests unless broken.
- There is currently no test runner or test configuration checked in under `admin-ui/`; do not assume Vitest, Cypress, or Playwright are already set up.

## Security

- Never log tokens or passwords.
- Validate all external input.
- Avoid using reflection unless necessary.
- Treat everything in `admin-ui/src`, `admin-ui/public`, and `admin-ui/index.html` as browser-visible; never hardcode secrets there.

## When in Doubt

If requirements are ambiguous, explicitly state assumptions before implementation.