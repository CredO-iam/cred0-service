# 0008 · Lessons learned

[Task](0008_GITHUB_TASK.md) · [Plan](0008_TASK_PLAN.md) · Documentation agent

## 2026-11-22 · Operator correction

The [workflow adoption](0005_AGENT_REPORT.md) mentioned handling corrections but
omitted DockMe's discoverable skill. Added the repository skill and relative
discovery link. AGENTS.md provides the trigger and manual fallback. The
procedure records corrections in the numbered task report.

Ran the skill-creator quick validator through the discovery path and verified
that the discovery and canonical paths resolve to the same file. Checked local
links and staged whitespace. Live selection in a fresh session was not exercised.

## 2026-11-22 · Packaging correction

The operator confirmed GitHub Copilot is retired. Moved the skill into
.agents/skills/lessons-learned/ and updated contributor references. Verified
the directory and skill file are stored directly, the old package is absent,
the skill validator passes and the documentation links resolve.
