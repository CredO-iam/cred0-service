## Title

Restore the lessons-learned procedure omitted from the DockMe workflow adoption.

## Background / Context

The operator identified the omission after task 0005. [Plan](0008_TASK_PLAN.md), revision 2

## Goal

Let a future agent act on corrective feedback using repository guidance alone.

## Scope

### In Scope

Package the skill, route agents to it and record the correction.

### Out of Scope

Global installation, changes to product behavior and retrospective edits to completed tasks.

## Detailed Implementation Requirements

Track SKILL.md directly under .agents/skills/lessons-learned/. Route from AGENTS.md and worklog/WORKFLOW.md. Use the
numbered agent report for dated evidence. No dependencies or API changes.

## Acceptance Criteria

The discovery directory contains the validated regular file. The workflow identifies when
to invoke the procedure and how to use it if absent from the registry.

## Assumptions / Open Decisions

Agent-owned repository maintenance. Codex is the intended consumer.

## Definition of Done

Validate metadata, resolve changed links, inspect the staged diff, record evidence
in the [report](0008_AGENT_REPORT.md) and open a pull request.
