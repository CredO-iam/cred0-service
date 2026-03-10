# AI Agent Policy

## General

- Follow existing project structure.
- Do not refactor unrelated code.
- Do not introduce breaking changes unless requested.

## Language

- All comments must be in English.
- No non-English text inside code.

## Architecture

- Follow clean layering.
- Do not mix business logic with controllers.
- Avoid framework leakage into domain.

## Dependencies

- Do not add new dependencies without explicit request.

## Tests

- All new logic must be covered by tests.
- Do not modify existing tests unless broken.

## Security

- Never log tokens or passwords.
- Validate all external input.
- Avoid using reflection unless necessary.

## When in Doubt

If requirements are ambiguous, explicitly state assumptions before implementation.