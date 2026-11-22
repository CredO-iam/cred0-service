# 0009 · Record password authentication outcomes

[Plan](0009_TASK_PLAN.md), revision 1 · Phase 3 · [MVP contract](0004_GITHUB_TASK.md)

## Background and goal

The form login authenticates local users. Operators need an observable record of accepted and rejected password authentication attempts.

## Scope and implementation

Add a listener in core security using the existing event infrastructure and logging backend. Give the two outcomes a stable event name and result field. Use fixed values so credentials, submitted identifiers, exception messages and session details cannot enter these records. Keep form login responses, database schema and the admin UI compatible.

## Dependencies and ownership

The Agent owns implementation, tests and documentation. Authentication and the bootstrap account already exist. Token policies and database versioning have separate architecture decisions.

## Acceptance and validation

- [x] [Agent] Unit tests distinguish password authentication from unrelated events.
- [x] [Agent] HTTP integration tests exercise accepted and rejected credentials and check exactly one expected audit record per submission.
- [x] [Agent] Run the backend suite, document the log contract and review the staged changes.

## Definition of done

Record results and release gaps in the [report](0009_AGENT_REPORT.md) and submit the reviewed change as a PR.
