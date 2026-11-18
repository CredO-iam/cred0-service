## Title

Adopt the DockMe task lifecycle for CredØ contributors.

## Background / Context

The operator requested the workflow identified in the DockMe comparison.
[Plan](0005_TASK_PLAN.md) · [Engineering harness](../CONCEPT.md), sections 31–37.

## Goal

Give the next contributor a reproducible route from approved work to verification and handoff.

## Scope

### In Scope

Repository instructions, workflow, task templates, contributor navigation and task records.

### Out of Scope

Product changes, release automation, external publication and historical task migration.

## Detailed Implementation Requirements

- Make AGENTS.md and README.md point to a single repository workflow.
- Provide reusable worklog templates for the plan, task and agent report.
- Preserve the global ID, paired artifacts and completion table. Cover task ownership,
  revisions, corrections, session evidence, operator steps and relevant validation.
- Preserve application contracts and dependency manifests.

## Acceptance Criteria

- A contributor can locate the current plan, next action, owner and validation evidence.
- Templates cover the existing task and report requirements in the harness.
- Changed local links resolve and task 0005 has exactly one index row.

## Assumptions / Open Decisions

Parent horizon is engineering harness maintenance. Executor and agent type are the
documentation agent. Dependencies are the current repository rules and existing worklog.

## Definition of Done

Verify the criteria using a link, template and index check and a staged whitespace check.
Record the results in the [agent report](0005_AGENT_REPORT.md).
