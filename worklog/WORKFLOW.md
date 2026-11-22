# Working on a CredØ task

## Start or resume

Read [repository rules](../AGENTS.md) and the relevant section of the
[engineering harness](../ENGINEERING_HARNESS.md). The [product context](../PRODUCT_CONTEXT.md)
owns scope and product decisions. The [README](../README.md) describes the shipped
surface and local setup. Check the owning contract and relevant ADR before editing code.

For an existing task read its latest dated report entry, current plan and acceptance
contract. Use the task's linked issue or PR for coordination when available.
The [index](worklog.md) lists completed tasks. Active plans can exist without an
index row. Follow the operator's current priorities and keep deferred work open.

## Plan and assign

1. Before implementation allocate the next four-digit ID above every numbered
   artifact and index entry, across all phases. Check active work for collisions.
2. Create both `NNNN_TASK_PLAN.md` and `NNNN_GITHUB_TASK.md` from the
   [plan](templates/TASK_PLAN.md) and [task](templates/GITHUB_TASK.md) templates.
   Replace placeholders, link the pair in both directions and link the originating
   requirement. Use [0001's plan](0001_TASK_PLAN.md) and [task](0001_GITHUB_TASK.md) for contract detail.
3. Give each outcome a checkbox and an executor (Agent, Operator or Agent + Operator).
   Record dependencies, affected paths, external behavior, risks and measurable
   acceptance checks. Resolve blocking decisions before marking the task Ready.
4. For material scope changes record approval, date and rationale in a new numbered
   revision section of the same plan. Preserve earlier revisions and identify the
   current one in the task. Update minor wording and checkboxes in place.
   Put independently deliverable future scope in a separate task when approved.

## Execute and hand off

Work through ready outcomes using the planned layer boundaries. Record progress in
`NNNN_AGENT_REPORT.md` using the [report template](templates/AGENT_REPORT.md).
Append a dated entry when handing over, pausing, changing a decision or completing
work. Include what changed, evidence, blockers and the next action with its owner.
Keep the report with its plan and task so another session can resume directly.

For operator-owned work provide the next actionable step and its expected result.
Wait for confirmation before dependent work. Identify operator-reported evidence
and keep credentials out of tasks, logs and chat. For corrections to completed work or recurring mistakes, follow the
[lessons-learned procedure](../.github/skills/lessons-learned/SKILL.md).

## Verify the outcome

Choose checks for the behavior changed and record the working directory, command,
result and remaining gaps. The harness quality gates in section 37 also apply.

| Change | Verification |
|---|---|
| Backend | Run `./gradlew test` in `core/`. Cover new logic with unit tests and changed endpoints with success and error cases. |
| Admin UI | Run `npm run build` in `admin-ui/` after installing dependencies. Exercise the affected browser interaction, including error feedback. Record the automated coverage gap where applicable. |
| OAuth2/OIDC | Check the relevant protocol contract and negative paths. For MVP acceptance run the full PKCE, JWKS and UserInfo journey defined in Product Context §4.3. |
| Deployment | Validate Compose configuration and the changed service's readiness. Record environment-specific deployment, restore and recovery evidence when claiming release readiness. |
| Documentation | Run `git diff --cached --check` after staging, resolve changed local links and check task traceability. |

## Close

Match acceptance criteria to evidence in the report. Tick verified outcomes only.
Record review, CI and PR references when available, including outstanding checks.
For a completed task add one linked row to the index using its ID and ISO completion
date. Preserve its three columns and earlier rows. Keep completed tasks in separate
commits, inspect the staged diff and include the ID and validation result in
`Task:` and `Validation:` trailers. Follow the harness branch and PR workflow
for delivery. Publication and deployment require the appropriate authorization.
