## Title

Give the reusable engineering harness a descriptive filename. [Plan](0007_TASK_PLAN.md)

## Background / Context

The operator observed that the generic filename could also describe a product concept.

## Goal

Identify the engineering document by its name in listings and references.

## Scope

### In Scope

Rename the root document and update repository references, including its own filename table.

### Out of Scope

Product decisions and changes to the engineering process.

## Detailed Implementation Requirements

The documentation agent will use ENGINEERING_HARNESS.md, matching the document title.
Update README.md, AGENTS.md, PRODUCT_CONTEXT.md and worklog references. No dependency or API changes.

## Acceptance Criteria

All tracked references use the new path and updated relative links resolve.

## Assumptions / Open Decisions

Documentation maintenance. The operator requested the rename and left the exact filename open.

## Definition of Done

Check references and staged whitespace, then record the result in the [report](0007_AGENT_REPORT.md).
