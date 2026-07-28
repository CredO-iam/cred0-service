# AI-Driven Engineering Harness

## Universal Engineering Approach for AI-Assisted Software Development

## 1. Purpose of This Document

This document defines a reusable engineering approach for developing software projects with AI as an active engineering participant.

The purpose is to establish a structured, repeatable process that can transform a high-level project idea into a working software product through coordinated AI-driven planning, analysis, implementation, testing, review, and release.

The engineering harness is intended to be reusable across different projects and domains.

The harness itself is independent of the specific software product being developed.

The product-specific information is maintained separately in a dedicated project document, such as:

* Project Brief;
* Product Context;
* Product Requirements;
* Domain Description.

That separate document describes:

* what product is being built;
* why it is being built;
* who it is for;
* product-specific constraints;
* product-specific goals;
* known technical direction.

This document describes **how the engineering work is organized and executed**, regardless of the product.

---

# 2. Core Concept

The engineering process is based on a collaboration model:

> Human Mentor / Product Owner + AI Engineering System

The human provides:

* intent;
* product direction;
* requirements;
* constraints;
* clarification;
* decisions.

AI performs the majority of engineering execution:

* project analysis;
* planning;
* requirements decomposition;
* architecture;
* task creation;
* implementation;
* testing;
* code review;
* documentation;
* coordination;
* progress tracking.

The fundamental principle is:

> AI manages the flow of engineering work. The human manages the direction of the product.

The human should not be required to manually coordinate every task or every AI agent.

The AI system should be autonomous within clearly defined boundaries and should proactively request human input only when necessary.

---

# 3. Design Goals

The engineering harness should provide the following capabilities.

## 3.1. Start From Intent

The human should be able to initiate a project using a high-level Project Brief.

The human is not expected to know in advance:

* the complete architecture;
* the complete requirements;
* the task breakdown;
* all dependencies;
* all implementation details;
* all risks.

The engineering harness should transform the initial intent into an executable engineering process.

---

## 3.2. Progressive Discovery

The system should progressively discover missing information.

The human should not be presented with hundreds of questions at the beginning.

Instead:

1. AI analyzes the available context.
2. AI identifies what is known.
3. AI identifies what can be safely inferred.
4. AI identifies genuine unknowns.
5. AI asks only the questions that require human input.
6. The answer becomes part of the project context.
7. AI continues the process.

This creates an iterative loop:

> Plan → Discover Unknown → Ask → Decide → Record → Continue

---

## 3.3. Autonomous Execution

Once requirements and dependencies are sufficiently clear, AI should proceed without unnecessary human intervention.

AI should be able to:

* create tasks;
* decompose tasks;
* define acceptance criteria;
* identify dependencies;
* assign work;
* implement code;
* create tests;
* create pull requests;
* perform code review;
* update documentation;
* detect risks.

---

## 3.4. Parallel Execution

The engineering process should support multiple AI agents working simultaneously.

Parallel work should be based on task dependencies.

Independent tasks should be executed in parallel.

Dependent tasks should wait until their prerequisites are resolved.

The goal is to maximize parallelism without sacrificing consistency.

---

## 3.5. Human-in-the-Loop Decisions

Human involvement should be triggered by uncertainty, not by routine workflow.

The human should be involved when:

* product requirements are ambiguous;
* multiple valid interpretations exist;
* product priorities affect the decision;
* requirements conflict;
* a significant scope decision is required;
* a major architectural choice depends on product intent;
* an irreversible or high-impact decision is required;
* AI cannot safely infer the correct behavior.

The human should not be interrupted for routine implementation choices that can be derived from existing project principles.

---

## 3.6. Dependency-Aware Workflow

The system should model work as a dependency graph rather than a flat list of tasks.

For each task, the system should understand:

* prerequisites;
* dependents;
* current status;
* blocking reasons;
* responsible agent;
* related requirements;
* related decisions;
* related code changes.

This allows the system to determine which work can proceed and which work must wait.

---

# 4. Human Role: Mentor

The human acts as:

* Product Owner;
* Customer;
* Mentor;
* final authority on product direction.

The Mentor is responsible for answering questions that require product judgment.

The Mentor is not responsible for manually managing the engineering pipeline.

The Mentor should not need to:

* create every issue;
* manually decompose every feature;
* assign every task;
* monitor every agent;
* track every dependency;
* manually synchronize decisions across documentation;
* manually notify agents that a decision has been made.

The engineering harness should automate these activities as much as possible.

The Mentor's primary responsibility is to ensure that the project moves in the correct direction.

---

# 5. AI Roles

The exact number and implementation of AI agents may vary by project.

The harness should support specialized AI responsibilities.

Possible roles include the following.

## 5.1. Project Initiator

The Project Initiator is responsible for starting the engineering process.

It receives the initial Project Brief and:

* analyzes the project intent;
* creates the initial project structure;
* establishes the initial engineering phases;
* identifies missing information;
* creates initial planning work;
* initializes documentation;
* prepares the project for execution.

The Project Initiator should not immediately begin unrestricted implementation.

Its first responsibility is to establish a structured process.

---

## 5.2. Planning Agent

The Planning Agent transforms high-level goals into executable engineering work.

Responsibilities include:

* requirements analysis;
* decomposition;
* identifying unknowns;
* identifying dependencies;
* defining acceptance criteria;
* identifying risks;
* creating issues;
* creating subtasks;
* proposing milestones.

---

## 5.3. Architecture Agent

The Architecture Agent is responsible for technical design.

Responsibilities may include:

* system architecture;
* component boundaries;
* domain model;
* data model;
* API design;
* security architecture;
* integration design;
* extension mechanisms;
* infrastructure architecture.

Architecture decisions should be based on established product requirements and project principles.

The Architecture Agent must not silently invent critical product requirements.

---

## 5.4. Development Agents

Development Agents execute ready-to-work tasks.

A development agent may:

* create a branch;
* implement code;
* write tests;
* update documentation;
* run local validation;
* create a pull request.

Multiple Development Agents may work concurrently.

---

## 5.5. Review Agent

The Review Agent performs automated engineering review.

Review should cover, as appropriate:

* correctness;
* requirements compliance;
* acceptance criteria;
* code quality;
* tests;
* security;
* architecture;
* maintainability;
* project conventions;
* documentation.

AI review should be performed before the work is considered complete.

---

## 5.6. Orchestrator

The Orchestrator coordinates the overall engineering process.

Responsibilities may include:

* monitoring project state;
* tracking dependencies;
* identifying ready tasks;
* assigning tasks to agents;
* detecting blocked work;
* triggering downstream work;
* coordinating parallel agents;
* identifying required human decisions;
* propagating decisions;
* maintaining consistency between planning and execution.

The exact architecture of the Orchestrator is a subject for Phase 0 design.

---

# 6. Project Initialization

The engineering process begins with a Project Brief.

The human provides:

* high-level intent;
* desired outcome;
* known constraints;
* known non-goals;
* initial assumptions;
* optional technology preferences.

The Project Brief is intentionally incomplete.

The purpose of the initial input is to provide direction, not to provide a complete specification.

The Project Initiator then begins the engineering process.

The initial flow is:

1. Receive Project Brief.
2. Analyze available context.
3. Create initial project structure.
4. Establish the first engineering phase.
5. Identify missing information.
6. Create initial planning tasks.
7. Establish the project execution framework.
8. Start progressive discovery.

The Project Brief should remain versioned and traceable.

---

# 7. Engineering Phases

The harness should organize work into explicit phases.

A generic project lifecycle may contain:

## Phase 0 — Engineering Harness

Define and establish the engineering process itself.

Topics include:

* roles;
* AI agents;
* project management;
* source of truth;
* documentation;
* issue lifecycle;
* dependency management;
* decision management;
* blocking mechanism;
* parallel execution;
* branch strategy;
* pull requests;
* AI review;
* CI/CD;
* Definition of Done;
* AI context and memory.

The output of Phase 0 is a working engineering process capable of executing the actual project.

---

## Phase 1 — Product / MVP Definition and Planning

Determine what must be known, decided, designed, implemented, tested, documented, and released to achieve the first milestone.

The exact milestone may be called:

* MVP;
* Proof of Concept;
* Beta;
* Release 1.0;
* Initial Production Release.

The phase should produce:

* requirements;
* unknowns;
* decisions;
* acceptance criteria;
* dependencies;
* risks;
* implementation work;
* validation work.

---

## Phase 2 — Architecture and Technical Design

Design the technical solution based on sufficiently defined requirements.

Possible outputs include:

* architecture;
* domain model;
* data model;
* APIs;
* security model;
* infrastructure;
* deployment model;
* testing strategy;
* extension model.

Important decisions should be recorded.

---

## Phase 3 — Implementation

Development Agents implement the planned work.

Work should be:

* dependency-aware;
* traceable to requirements;
* traceable to design decisions;
* independently executable where possible.

Independent tasks should be implemented in parallel.

---

## Phase 4 — Integration and Validation

Validate the system as a whole.

Validation should include, where applicable:

* functional behavior;
* integration;
* end-to-end workflows;
* security;
* performance;
* deployment;
* configuration;
* compatibility;
* upgrade behavior.

---

## Phase 5 — Release

Prepare the milestone for release.

Possible activities include:

* versioning;
* release notes;
* documentation;
* deployment artifacts;
* installation instructions;
* examples;
* known limitations;
* migration information.

---

## Phase 6 — Post-Release / Next Milestone

After release:

* collect feedback;
* analyze issues;
* identify improvements;
* prioritize backlog;
* define the next milestone.

---

# 8. Phase-Based Planning

Each phase should be treated as a structured set of questions and decisions.

The system should not jump directly from a high-level idea to implementation tasks.

Instead, the process is:

1. Define the objective of the phase.
2. Identify what must be known to achieve it.
3. Identify known information.
4. Identify unknown information.
5. Determine which unknowns can be inferred.
6. Identify unknowns requiring human decisions.
7. Ask the human progressively.
8. Record decisions.
9. Update project context.
10. Decompose the resulting work.
11. Create tasks.
12. Establish dependencies.
13. Begin execution.

This approach ensures that architecture and implementation questions emerge at the appropriate stage.

---

# 9. Decision Requests

When an AI agent encounters an unresolved issue that requires human input, it should create a structured Decision Request.

A Decision Request should include:

* title;
* question;
* context;
* reason the decision is required;
* current assumptions;
* available options;
* advantages and disadvantages;
* AI recommendation;
* impact of each option;
* affected requirements;
* affected architecture;
* affected tasks;
* urgency, if relevant.

Example structure:

```text
Decision Request

Question:
[Specific question]

Context:
[Relevant background]

Why this decision is needed:
[Explanation]

Options:
A. [Option]
B. [Option]
C. [Other]

AI Recommendation:
[Recommendation]

Impact:
[Components/tasks affected]

Blocking:
[Tasks currently blocked]
```

The question should be specific enough that the human can make an informed decision without reconstructing the entire project context.

---

# 10. Blocking Model

A blocked task must not necessarily block the entire project.

When a task encounters an unresolved decision:

1. The task becomes BLOCKED.
2. The reason is recorded.
3. The required decision is created.
4. The relevant human is notified.
5. Dependent tasks may also become blocked.
6. Independent tasks remain executable.

The system should calculate the impact of the blocking decision.

This enables continued progress while waiting for human input.

---

# 11. Decision Propagation

When the human answers a Decision Request, the decision becomes part of the authoritative project context.

The system should then:

1. Record the decision.
2. Update requirements.
3. Update architecture documents.
4. Create or update an ADR if appropriate.
5. Update affected issues.
6. Update acceptance criteria.
7. Recalculate dependencies.
8. Unblock eligible tasks.
9. Notify affected agents.
10. Resume execution.

The human should not be responsible for manually propagating the decision.

---

# 12. Mentor Inbox

The Mentor Inbox is the primary conceptual entry point for the human.

It should show only items requiring human attention.

Possible categories include:

* Blocking Questions;
* Product Decisions;
* Architecture Decisions;
* Approval Requests;
* High-Risk Changes;
* Critical Reviews;
* Escalations.

The system should proactively notify the Mentor when an action is required.

The intended workflow is:

> The Mentor does not continuously monitor the project to find problems. The engineering system brings relevant decisions to the Mentor.

The Mentor should be able to:

* inspect the context;
* understand the consequences;
* make a decision;
* approve or reject a proposal;
* provide additional requirements.

---

# 13. Project Dashboard

The Mentor should also have access to a high-level project view.

The dashboard should provide:

* current phase;
* milestone progress;
* active work;
* completed work;
* blocked work;
* pending decisions;
* active AI agents;
* critical dependencies;
* major risks;
* recent decisions;
* overall project health.

The dashboard is for situational awareness.

The Mentor Inbox is for action.

These should be conceptually separate.

---

# 14. Parallel Execution

The engineering harness should support concurrent work.

Parallel execution is allowed when:

* dependencies are satisfied;
* the work does not create conflicting changes;
* required decisions are already available;
* agents have sufficient context.

Potential parallel workstreams may include:

* backend;
* frontend;
* infrastructure;
* testing;
* documentation;
* architecture;
* developer tooling.

The Orchestrator should coordinate these workstreams.

---

# 15. Synchronization

Parallel execution must not result in fragmented project knowledge.

The project must maintain a shared source of truth.

Agents should be able to access:

* project brief;
* current requirements;
* architectural decisions;
* ADRs;
* coding conventions;
* task dependencies;
* current milestone;
* relevant previous decisions.

When a decision changes the project direction, affected agents must receive the updated context.

---

# 16. Source of Truth

The engineering harness should explicitly define authoritative sources.

A possible model is:

* Git repository: source code and versioned project documentation;
* Project management system: executable work items and status;
* Requirements: product intent and acceptance criteria;
* ADRs: architectural decisions;
* Decision records: Mentor decisions;
* CI/CD: automated quality gates.

The exact tooling is a Phase 0 decision.

The fundamental requirement is:

> The same critical information must not exist in conflicting versions across multiple systems.

---

# 17. Project Management Tooling

The harness should be tool-independent.

Possible tools include:

* GitHub Issues;
* GitHub Projects;
* GitLab Issues;
* Jira;
* Linear;
* other task management systems.

The choice should be based on project needs.

The tool must support, directly or through automation:

* milestones;
* tasks;
* subtasks;
* dependencies;
* blocked status;
* ownership;
* links to code;
* pull requests;
* automation;
* AI agent integration;
* project visibility.

For smaller projects, a Git-based platform may be sufficient.

For larger teams or organizations, a dedicated project management system may be appropriate.

The decision should be made during Phase 0.

---

# 18. Task Lifecycle

A generic task lifecycle may include:

* Proposed
* Planned
* Ready
* In Progress
* Blocked
* In Review
* Changes Requested
* Approved
* Done
* Cancelled

The exact state model should be defined during Phase 0.

The workflow should avoid unnecessary states.

Each state should have a clear meaning and transition criteria.

---

# 19. Branch and Pull Request Workflow

The engineering harness should support isolated implementation.

A typical flow is:

1. Task becomes Ready.
2. Development Agent claims task.
3. Agent creates branch.
4. Agent implements changes.
5. Agent writes/updates tests.
6. Agent runs validation.
7. Agent creates Pull Request.
8. Review Agent reviews changes.
9. CI runs.
10. Changes are requested if necessary.
11. Agent addresses feedback.
12. PR is approved.
13. Changes are merged.
14. Task is marked Done.

The exact Git workflow is a Phase 0 decision.

---

# 20. AI Code Review

AI review should be an explicit part of the engineering process.

The reviewer should have access to:

* original requirements;
* acceptance criteria;
* relevant architecture decisions;
* task description;
* code changes;
* tests.

The reviewer should check whether the implementation satisfies the intended behavior, not merely whether the code compiles.

Review should identify:

* functional defects;
* missing tests;
* security problems;
* architectural violations;
* unnecessary complexity;
* maintainability issues;
* deviations from requirements.

The review process should produce actionable feedback.

---

# 21. Definition of Done

The project should establish a Definition of Done.

A task should generally not be considered complete until applicable criteria are satisfied.

Potential criteria include:

* implementation completed;
* requirements satisfied;
* tests added;
* tests passing;
* code reviewed;
* security considerations addressed;
* documentation updated;
* CI passing;
* related decisions recorded.

The exact Definition of Done must be defined during Phase 0.

---

# 22. AI Context and Project Memory

AI agents must have access to consistent project context.

The harness should maintain structured project knowledge.

Potential information includes:

* Project Brief;
* current requirements;
* project principles;
* architecture;
* ADRs;
* Mentor decisions;
* coding conventions;
* development workflow;
* current milestone;
* task dependencies;
* known risks;
* known assumptions.

AI agents should not rely solely on conversation history.

Important project knowledge should be persisted in versioned or otherwise authoritative project artifacts.

---

# 23. Assumptions and Decisions

The harness should distinguish between:

* confirmed requirements;
* confirmed decisions;
* assumptions;
* inferred behavior;
* proposed decisions;
* unresolved questions.

AI must not treat an assumption as a confirmed requirement.

Every important assumption should have a clear status.

When an assumption is invalidated, affected work should be identified.

---

# 24. AI Autonomy Boundaries

AI should be autonomous within established project constraints.

AI may independently make routine technical decisions when they:

* do not change product behavior;
* do not change scope;
* do not conflict with established architecture;
* do not create significant irreversible consequences.

AI should escalate decisions involving:

* product behavior;
* user experience with significant impact;
* scope;
* security requirements;
* compliance;
* major architecture;
* irreversible data changes;
* significant operational consequences.

The goal is to avoid both extremes:

* AI asking the human about every minor detail;
* AI silently making decisions that belong to the product owner.

---

# 25. Central Engineering Loop

The entire engineering harness is based on a continuous loop:

> Plan
> → Discover Unknowns
> → Ask Mentor
> → Record Decision
> → Update Context
> → Decompose
> → Identify Dependencies
> → Execute in Parallel
> → Review
> → Validate
> → Integrate
> → Repeat

This loop continues throughout the project lifecycle.

The process is not strictly linear.

New information discovered during implementation may cause:

* new questions;
* requirement changes;
* architecture changes;
* new tasks;
* dependency changes.

The harness must support this feedback loop.

---

# 26. Initial Engineering Harness Phase

The first phase of applying this methodology to any project should be:

# Phase 0 — Engineering Harness

The purpose is to establish the engineering process before significant implementation begins.

The Phase 0 plan should answer:

1. How is a project initialized?
2. How is the Project Brief stored?
3. How are Mentor and AI roles defined?
4. Which AI agent roles are required?
5. How do agents communicate?
6. What is the source of truth?
7. Which project management tool is used?
8. Are GitHub Issues sufficient?
9. Is a project board required?
10. Is a dedicated project management system required?
11. How are dependencies represented?
12. How are blocked tasks represented?
13. How are Decision Requests created?
14. How is the Mentor notified?
15. How are decisions recorded?
16. How are decisions propagated?
17. How is parallel execution coordinated?
18. How are agents assigned work?
19. How are branches managed?
20. How are Pull Requests handled?
21. How does AI code review work?
22. What is the Definition of Done?
23. What quality gates are required?
24. How does CI/CD work?
25. How is project context maintained?
26. How is AI memory managed?
27. How are failed agents handled?
28. How are abandoned tasks recovered?
29. How are conflicts resolved?
30. How is the transition from planning to implementation handled?
31. What makes the Engineering Harness Ready?

The result of Phase 0 should be a defined and operational process that can be reused for the actual product development.

---

# 27. Transition to Product Development

Once the Engineering Harness is ready, the project-specific Product Brief is introduced into the process.

The harness then begins:

# Phase 1 — Product / MVP Definition and Planning

The purpose of this phase is to identify all questions that must be answered to reach the first meaningful milestone.

The AI should:

1. Analyze the Product Brief.
2. Identify known requirements.
3. Identify explicit constraints.
4. Identify non-goals.
5. Identify unknowns.
6. Identify assumptions.
7. Determine which unknowns can be inferred.
8. Generate questions requiring human input.
9. Ask questions progressively.
10. Record decisions.
11. Build the requirements model.
12. Decompose work.
13. Create tasks.
14. Identify dependencies.
15. Prepare the project for implementation.

The product-specific questions and requirements belong to the Product / MVP planning process and should not be hardcoded into the generic engineering harness.

---

# 28. Separation Between Harness and Product

The engineering harness must remain independent of the specific product.

The harness defines:

* how work is planned;
* how questions are asked;
* how decisions are made;
* how work is decomposed;
* how agents operate;
* how tasks are coordinated;
* how parallel execution works;
* how blocked work is handled;
* how code is reviewed;
* how project knowledge is maintained.

The Product Brief defines:

* what is being built;
* why it is being built;
* who it is for;
* what constraints apply;
* what the product should do.

The two should be connected but kept conceptually separate.

This separation allows the same engineering harness to be reused for different projects.

---

# 29. Desired End State

The final engineering system should make the following workflow possible:

A human provides a high-level project idea.

The AI system:

1. Initializes the project.
2. Establishes the engineering process.
3. Analyzes the project context.
4. Creates phases.
5. Identifies unknowns.
6. Asks the human only necessary questions.
7. Records decisions.
8. Decomposes work.
9. Creates tasks.
10. Builds dependencies.
11. Starts multiple AI agents.
12. Executes independent work in parallel.
13. Pauses blocked branches.
14. Continues unrelated work.
15. Requests human decisions when required.
16. Propagates decisions automatically.
17. Resumes blocked work.
18. Reviews implementations.
19. Runs automated validation.
20. Integrates completed work.
21. Tracks progress.
22. Produces a release.
23. Learns from the result.
24. Begins the next milestone.

The human remains the owner of product direction.

AI becomes the primary executor and coordinator of engineering work.

The system should minimize the amount of manual project management required from the human while maintaining human control over important decisions.

The ultimate objective is:

> Enable one human Mentor to guide complex software development through a coordinated system of specialized AI agents, while preserving engineering discipline, traceability, parallel execution, and human control over product intent.

The specific product being developed and the product-specific application of this methodology are documented separately.

---

# 30. Product Context Contract

The generic harness is applied together with a separate Product Context
document. In this repository, the product context is
[`PRODUCT_CONTEXT.md`](./PRODUCT_CONTEXT.md).

The documents have different responsibilities:

| Document | Authority |
|---|---|
| `CONCEPT.md` | Engineering workflow, AI roles, phases, decisions, dependencies, execution, review, and project memory |
| `PRODUCT_CONTEXT.md` | CredØ product vision, users, scope, constraints, technology direction, and unresolved product questions |
| Architecture documents and ADRs | Approved technical design decisions |
| Issues and pull requests | Executable work, implementation details, and delivery status |

The harness must read and use the Product Context when planning product work,
but must not absorb product-specific assumptions into the reusable harness.
Conversely, a product requirement must not be changed by an implementation
choice without an explicit product decision.

Each project phase should therefore distinguish:

1. reusable engineering rules from product-specific requirements;
2. confirmed decisions from assumptions and open questions;
3. product decisions from technical implementation decisions;
4. the source document that must be updated when a decision changes.

This contract makes the connection explicit while preserving the ability to
reuse the engineering harness for another product.

---

# 31. Operational Workflow

The engineering process is initiated by the Mentor, author, or developer by
declaring a planning horizon, such as `MVP`, a release, or another milestone.
The horizon is recorded in a Project Brief and opened as an executable
planning item in GitHub Issues.

After the horizon is declared, agents operate autonomously:

1. The Project Initiator reads the Project Brief and current Product Context.
2. Planning agents identify requirements, unknowns, risks, dependencies, and
   decisions.
3. Agents create and refine executable tasks.
4. A task becomes `Ready` only when its readiness criteria are satisfied.
5. Development agents claim and execute ready tasks.
6. Review agents and CI validate the resulting changes.
7. Agents move tasks through the lifecycle when the applicable transition
   criteria are satisfied.
8. Completion of a task makes eligible dependent tasks available.
9. The phase is complete when all tasks in the phase are in a terminal state
   and no unresolved blocking decision or dependency remains.

The Mentor is not required to start individual agents or manually move routine
tasks. The Mentor is involved when the escalation rules require a decision or
approval.

## 31.1. Task Readiness

The planning process must define measurable readiness criteria for every task.
At minimum, a task is `Ready` only when it has:

- a clear objective and scope;
- links to the originating requirement or decision;
- acceptance criteria;
- an identified owner or agent type;
- known dependencies;
- no unresolved blocking decision;
- enough technical context to begin without redefining product intent.

## 31.2. Task Completion

An agent may mark a task complete when its acceptance criteria and applicable
Definition of Done checks are satisfied. The Orchestrator reconciles the task
with the linked Pull Request, review result, and CI status before treating it
as complete and releasing dependent work.

---

# 32. Project Management and Notifications

GitHub is the primary management platform:

- GitHub Issues are the source of executable work items and Decision Requests;
- GitHub Projects provide the operational board, views, filters, and progress
  reporting;
- Pull Requests represent implementation delivery and review;
- GitHub Actions run orchestration, validation, and synchronization;
- the repository contains versioned requirements, decisions, plans, ADRs, and
  agent reports.

Decision Requests are stored in the repository under `worklog/` and are also
represented by a GitHub Issue so that the Mentor can be notified and tagged.
The repository artifact is the durable record; the Issue is the interaction
and workflow surface.

The Mentor is notified by tagging the configured Mentor account in the
Decision Request Issue. The Issue must link to the repository artifact and
include enough context for a decision without reconstructing the entire
project history.

---

# 33. Standard Project Artifacts

The following artifact formats are proposed as the initial standard.

## 33.1. Project Brief

Path: `worklog/{NNNN}_PROJECT_BRIEF.md`

Required sections:

- `Title`
- `Planning Horizon`
- `Background / Problem`
- `Desired Outcome`
- `Intended Users`
- `Known Requirements`
- `Known Constraints`
- `Known Non-Goals`
- `Initial Technical Direction`
- `Open Questions`
- `Acceptance Criteria for the Horizon`
- `Status and Decision History`

The Project Brief describes intent. It must not pretend to be a complete
technical specification.

## 33.2. Executable Task

Path: `worklog/{NNNN}_GITHUB_TASK.md`, paired with
`worklog/{NNNN}_TASK_PLAN.md` where planning detail is needed.

Required sections:

- `Title`
- `Parent Phase / Horizon`
- `Background / Context`
- `Goal`
- `Scope`
- `Requirements and Constraints`
- `Dependencies`
- `Agent Type`
- `Acceptance Criteria`
- `Validation Plan`
- `Risks`
- `Definition of Done`
- `Links`

The GitHub Issue must link to the artifact, and the artifact must link to the
originating requirement, decision, and resulting Pull Request when available.

## 33.3. Decision Request

Path: `worklog/{NNNN}_DECISION_REQUEST.md`

Required sections:

- `Title`
- `Question`
- `Context`
- `Why a Decision Is Required`
- `Current Assumptions`
- `Options`
- `Advantages and Disadvantages`
- `AI Recommendation`
- `Impact`
- `Affected Requirements, Architecture, and Tasks`
- `Blocking Scope`
- `Mentor Decision`
- `Decision Date`
- `Propagation Status`

The corresponding GitHub Issue must tag the Mentor and use the
`decision-needed` label.

## 33.4. Architecture Decision Record

Path: `docs/adr/{NNNN}-{short-name}.md`

Required sections:

- `Title`
- `Status`
- `Date`
- `Context`
- `Decision`
- `Alternatives Considered`
- `Consequences`
- `Affected Product Context`
- `Affected Tasks and Components`
- `Supersedes / Superseded By`

An ADR is required for an approved architectural decision that can influence
future product evolution. A Decision Request may be resolved without an ADR
when the decision is local and non-architectural.

## 33.5. Agent Report

Path: `worklog/{NNNN}_AGENT_REPORT.md`

Required sections:

- `Task`
- `Agent`
- `Started`
- `Completed`
- `Work Performed`
- `Files or Components Changed`
- `Acceptance Criteria Results`
- `Validation Results`
- `Known Limitations`
- `Discovered Questions or Risks`
- `Follow-Up Tasks`
- `Pull Request`

The report is produced before the task is considered complete and provides a
compact handoff for review and downstream agents.

## 33.6. Mandatory Traceability

The minimum traceability chain is:

> Project Brief → Requirement → Decision or Task → Pull Request → Review and
> CI → Agent Report → Phase Status

Every task must identify its parent phase and originating requirement. Every
Decision Request must list affected tasks and documents. Every ADR must link
back to the Decision Request or explicitly state why it was created directly.
No critical product or architecture decision is valid without a durable
repository record.

---

# 34. Formal Work Model

The initial logical model contains the following entities.

| Entity | Required fields |
|---|---|
| Project Brief | id, title, horizon, objective, users, requirements, constraints, non-goals, status, owner, created, updated |
| Phase | id, name, objective, entry criteria, exit criteria, status, parent horizon, task references |
| Requirement | id, statement, type, source, priority, acceptance criteria, status, assumptions, linked decisions and tasks |
| Task | id, title, phase, objective, scope, status, priority, agent type, owner, acceptance criteria, validation plan, dependencies, blockers, links |
| Dependency | id, predecessor, successor, type, reason, status |
| Decision | id, question, category, status, options, recommendation, decision, decider, date, affected artifacts, propagation status |
| Blocker | id, task, reason, blocker type, blocking artifact, affected dependents, status, created, resolved |
| Agent | id, role, capability, runtime, current task, status, branch or workspace, last activity |
| Risk | id, description, probability, impact, severity, owner, mitigation, affected artifacts, status |

Statuses must be finite, documented, and used consistently. An implementation
may store these entities in GitHub Issues, Projects, labels, repository files,
and workflow metadata, but the semantics must remain stable.

---

# 35. Orchestration Through GitHub

The initial Orchestrator should run as a dedicated GitHub Actions workflow.
It may be triggered manually for a new planning horizon and automatically by
relevant Issue, Pull Request, workflow, and project-state events. A scheduled
reconciliation run may be added to detect state drift.

The Orchestrator should:

1. read the current Project Brief, Product Context, project rules, and
   repository artifacts;
2. find tasks in `Ready` state;
3. exclude tasks with unresolved dependencies, blockers, or required Mentor
   decisions;
4. select tasks by phase, priority, readiness age, and available agent
   capability;
5. claim tasks atomically using GitHub assignees, labels, and a concurrency
   group;
6. dispatch the appropriate GitHub Agent or workflow;
7. monitor task, branch, Pull Request, review, and CI state;
8. reconcile completed work;
9. transition eligible dependent tasks to `Ready`;
10. create or update a Mentor Decision Request when escalation is required.

## 35.1. Agent Assignment

Tasks declare an `Agent Type` and required capabilities. The Orchestrator
matches those requirements to available agents. Examples include planning,
architecture, backend development, frontend development, testing,
documentation, and review.

Assignment must be explicit in the task and visible in GitHub. An agent must
not select a task whose required capability it does not provide.

## 35.2. Preventing Double Work

Before dispatch, the Orchestrator claims a task by setting the assignee,
`in-progress` label, agent metadata, and a unique concurrency key derived from
the task ID. A second dispatch for the same task is rejected while the claim
is active.

The claim must identify the branch or Pull Request. A task cannot be claimed
by another agent until it is released, completed, or explicitly requeued.

## 35.3. Detecting Completion

Completion is detected from the combined state of:

- agent report;
- acceptance criteria;
- Pull Request;
- required review;
- required CI checks;
- task state.

The Orchestrator must not release dependent work based only on an agent's
message or a changed task label.

## 35.4. Starting Dependent Work

When a predecessor reaches `Done`, the Orchestrator recalculates the
dependency graph. A successor is moved to `Ready` only when all predecessors
are complete, all readiness criteria are satisfied, and no blocker or
unresolved decision remains.

---

# 36. Escalation and Approval Rules

AI may decide routine implementation details when they preserve product
behavior, scope, security requirements, and established architecture.

Mentor input is mandatory when a decision:

- changes product behavior or scope;
- resolves an ambiguous product requirement;
- introduces or changes a security or compliance requirement;
- affects the architecture;
- creates a public API or extension contract;
- can materially influence future product development;
- creates an irreversible or high-impact consequence.

Critical approval is additionally required before merging changes that:

- establish or change a foundational architecture;
- alter authentication, authorization, token, or security boundaries;
- perform irreversible data or migration operations;
- change release scope or declare a milestone complete.

The escalation record must identify the affected work and block only the
dependent branch. Independent work continues.

---

# 37. Initial Quality Gates

Every completed task must satisfy the applicable gates:

- acceptance criteria are explicitly checked;
- relevant tests are added or updated;
- applicable build and lint checks pass;
- a Review Agent evaluates the change;
- documentation and traceability are updated;
- CI status is successful.

Additional gates apply by task type:

| Task type | Additional required gates |
|---|---|
| Backend or frontend behavior | Automated tests for changed logic and integration checks where applicable |
| Authentication, authorization, tokens, credentials, or security configuration | Security-focused review and relevant negative-path tests |
| Public API or extension point | Contract compatibility review and documentation |
| Architecture or cross-cutting change | Approved ADR and critical Mentor approval |
| Deployment, configuration, or operational change | Reproducible validation and operational documentation |
| Documentation-only change | Link/consistency review and traceability validation |

The exact commands are project-specific and must be declared in the task or
repository instructions. Recovery from failed or abandoned agent work is
intentionally deferred and is not required for the first harness definition.

---

# 38. Phase 0 Decisions Still Open

The operational baseline above is sufficient to design the harness, but the
following items remain to be selected or implemented:

- the exact GitHub Project fields, views, and labels;
- the concrete GitHub Agent dispatch mechanism;
- the available agent types and capability registry;
- the exact repository filename conventions and numbering policy;
- the CI checks for each repository layer;
- the first working Orchestrator workflow;
- the minimum MVP scope of the harness itself.

The final item must be prepared interactively rather than assumed from this
concept. Phase 0 is ready only after these choices are recorded and the
selected workflow has been exercised on at least one complete planning-to-PR
cycle.
