# ABOUTME: This file defines the core principles and rules for Claude-assisted development.
# ABOUTME: Adherence to these rules is mandatory for all generated and modified code.

# Writing Code

- We prefer simple, clean, maintainable solutions. Readability and long-term maintainability are primary concerns over premature optimization or overly complex conciseness.
- Make the smallest reasonable, targeted changes to achieve the desired outcome. You MUST ask for explicit user permission before reimplementing existing features or systems from scratch.
- When modifying code, strictly match the style and formatting of surrounding code. Consistency within a file is paramount. For new files, adhere to the project's `ktlint` configuration.
- NEVER make code changes that aren't directly related to the task you're currently assigned. If you notice something that should be fixed but is unrelated to your current task, document it for the human to create a new issue. DO NOT FIX IT.
- NEVER remove existing code comments unless you can definitively prove they are factually incorrect and misleading. Comments are considered vital documentation. If clarification is needed, augment, don't remove.
- When writing comments, avoid referring to temporal context (e.g., "recently refactored", "old code"). Comments MUST be evergreen and describe what clean code can not express.
- NEVER name things (variables, classes, methods, files) with prefixes or suffixes like 'improved', 'new', 'enhanced', 'V2', 'temp', etc. All naming MUST be evergreen and reflect function, not history.
- When you are trying to fix a bug, compilation error, or any other issue, YOU MUST NEVER discard the existing implementation and rewrite it from scratch without explicit permission from the user. If you believe a rewrite is necessary, YOU MUST STOP and get explicit permission first.

# TCR Development Approach

- **Progressive Implementation:** Start with the simplest possible implementation that satisfies the requirements.
- **Primitive Values First:** Start with primitive types (String, Int, etc.) in your initial implementation. Only introduce value objects, entities, and other domain model refinements AFTER the basic functionality is working and tested.
- **Respond to TCR Failures:** When a TCR cycle fails (reverts your changes), ALWAYS reduce the scope of your changes in the next attempt. Break the change into smaller, more focused changes that can be independently verified.
- **TCR Revert Detection:** Pay close attention to revert messages in the TCR output. When you see "HEAD is now at [commit] [TCR RESET]", it indicates your changes were reverted. In response, you MUST take smaller steps in your next attempt.
- **Incremental Modeling:** Do not try to create a sophisticated domain model upfront. Start with simple data structures and gradually evolve them as the requirements become clearer and tests validate their behavior.
- **Avoid Premature Abstraction:** Implement concrete functionality first. Only create abstractions when you have multiple concrete implementations or when the abstraction is directly required by the current task.

Find more details about general testing philosophy in @.claude/docs/tdd.md

# Kotlin & Spring Boot Specifics

- **Immutability:** Always prefer `val` over `var`. Collections should be read-only by default where possible (e.g., `List` instead of `MutableList` in public APIs, unless mutability is essential to the component's internal workings).
- **Data Classes:** Use data classes for DTOs and simple value objects.
- **Null Safety:** Leverage Kotlin's null safety. Avoid platform types. Use `?.` and `?:` appropriately. Explicitly non-null assertions (`!!`) are forbidden unless accompanied by a comment explaining why nullability is impossible in that specific context AND approved by the human. DO NOT write tests for null handling as Kotlin provides compile-time null safety - parameters should be non-nullable by default unless there's a specific requirement for optional values.
- **Default Parameters:** Use default parameters for dependencies in service classes to reduce duplication in creating instances. This makes testing more flexible by allowing test code to either use the defaults or provide custom mocked dependencies.
- **Configuration:** Use `application.yml` (or `application.properties` if YAML is not available) for configuration. Profile-specific configurations are encouraged (`application-dev.yml`, `application-test.yml`).
- **API Design:** REST APIs MUST use DTOs for request and response bodies. Entities should not be directly exposed. Use standard HTTP verbs and status codes correctly. Version APIs (e.g., `/api/v1/...`).
- **Error Handling:** Implement global error handling using `@ControllerAdvice` and `@ExceptionHandler`. Return consistent error response DTOs.
- **Kotlin Version:** Target Kotlin 2.1.21.
- **Java Version:** Target Java 21 LTS.

# Domain-Driven Design Principles

- **Work with existing domain classes:** NEVER create duplicate or parallel implementations of domain concepts. For example, if there's a `Timeline` class, don't create a `TimelineAggregate` class that does the same thing.
- **Understand DDD terminology correctly:**
  - **Entities:** Domain objects with identity that changes over time (usually has an ID)
  - **Value Objects:** Immutable objects defined by their attributes (no identity)
  - **Aggregates:** Clusters of entities and value objects with a clear boundary and a single entity as the root
  - **Repositories:** Interfaces for retrieving and persisting aggregates
  - **Services:** Operations that don't naturally belong to entities or value objects
- **Repository naming convention:** A repository for an aggregate is named after the aggregate root (e.g., `TimelineRepository` for the `Timeline` aggregate)
- **Refactoring approach:** When implementing DDD principles, modify existing classes to align with DDD patterns rather than creating parallel implementations. For example, refactor a `Timeline` class to be an aggregate root rather than creating a new `TimelineAggregate` class.
- **Complete refactoring:** When refactoring to improve implementation (e.g., replacing an inline data structure with a proper class), always remove the old implementation in the same step. Never leave both old and new ways of fulfilling the same use case, as this increases complexity and maintenance burden.
- **Information hiding:** Reduce API surface area by properly encapsulating implementation details. Only expose what clients actually need to use, hiding the rest as internal/private.
- **Always examine the codebase completely before introducing new concepts or classes** to avoid duplication and confusion.

## Package Structure

When creating new files, **always** place them in the correct package following this structure:

```
org.codingkata.socialnetwork
├── domain                      # Core domain model (entities, aggregates, value objects)
│   ├── [bounded-context]       # e.g., user, timeline, post
│   │   ├── model               # Domain model classes (entities, value objects)
│   │   ├── repository          # Repository interfaces
│   │   └── service             # Domain services
├── application                 # Application services that orchestrate domain objects
│   ├── service                 # Application services 
│   └── dto                     # Data Transfer Objects
├── infrastructure              # Technical implementations
│   ├── repository              # Repository implementations
│   └── persistence             # Database-specific code
└── presentation                # User interface (if applicable)
    └── controller              # API controllers
```

Key guidelines:
- Create all new files directly in their correct package location
- Name subdirectories using singular form (e.g., `repository` not `repositories`)
- Group related domain concepts in bounded contexts (e.g., `user`, `timeline`, `post`)
- Keep domain logic free from application or infrastructure dependencies
- Domain model classes should never import from application or infrastructure packages
- When naming files, include their role in the name (e.g., `UserRepository.kt`, not just `User.kt`)
- Tests should mirror the package structure of the code they test

# Getting Help & Clarification

- ALWAYS ask for clarification from the human rather than making assumptions, especially if a requirement in `spec.md` is ambiguous or conflicts with rules in this `Claude.md`.
- If you encounter significant difficulty or a task seems outside the scope of effective LLM generation (e.g., complex multi-system orchestration debugging), it is mandatory to stop and ask for human assistance.

# Testing

- **TDD (Test-Driven Development) + TCR (Test commit revert) is MANDATORY:**
  1. Write tests *before* writing the implementation code.
  2. Only write the minimal implementation code required to make the currently failing test pass.
  3. Refactor code continuously, ensuring all tests remain green.
  4. `./tcr.sh -c` executes a tcr cycle creating a commit when tests are green, or reverts all changes on a failed test or not fixable ktlint violation with an empty commit with a description why a tcr cycle failed.
- Tests MUST cover the functionality being implemented, including edge cases and error conditions.
- Tests like "check edge cases" should be avoided. Instead, tests should be very specific to a case and assert a single behavior.
- NEVER ignore the output of the system or the tests. Logs and messages often contain CRITICAL information for debugging.
- TEST OUTPUT MUST BE PRISTINE TO PASS. No unexpected errors or warnings in logs during test runs.
- If logs are *supposed* to contain specific error messages as part of a test scenario (e.g., testing error handling), these MUST be captured and asserted.
- DO NOT add meaningless comments in tests like "// Arrange", "// Act", "// Assert". Let the test code speak for itself through clear structure and naming.
- **Test Positioning and Scope:**
  - Place the majority of tests at the service/application layer rather than at the individual class level
  - Tests should primarily focus on behaviors and use cases rather than implementation details
  - Minimize tests that are tightly coupled to implementation details, as these make refactoring difficult
  - Lower-level tests are appropriate only when:
    1. Testing complex algorithms with many edge cases
    2. Testing reusable infrastructure components
    3. The logic is genuinely isolated and unlikely to be refactored between classes
  - When refactoring to move logic between classes, tests should require minimal changes
  - When refactoring implementation details, DO NOT add new tests - existing tests should verify correctness
  - Avoid excessive unit tests at the single class level to enable future refactoring without test maintenance burden
- Focus on Behavior and Contracts: Good tests define and verify the expected behavior of a system, service, class, or method. They focus on the contract or public interface exposed to consumers. They describe what the system does when given inputs and what outputs or state changes result, rather than verifying internal method calls or interactions with mocked dependencies.
- Driven by Business Requirements/Acceptance Criteria: The trigger for writing a new test should be a new behavior or requirement specified by the business or product owner. Developers use concrete examples from these requirements to guide their test writing.
- Readable and Self-Documenting: Good tests should be readable and act as executable documentation. They should use domain language (the language of the business) and have descriptive names that clearly articulate the expected behavior being tested. They should follow clear structures like Arrange-Act-Assert (AAA) or Given-When-Then (GWT).
- Non-Brittle: Good tests should be decoupled from the internal structure and implementation details of the code. Changing the internal implementation of a system or class should not require changing its tests, as long as the observable behavior remains the same. Brittle tests that break whenever internal code changes indicate they are testing implementation details rather than behavior.
- Fast and Independent: Tests should run quickly (ideally within seconds) and be isolated from each other so they can run in parallel. This ensures a fast feedback loop, which is crucial for TDD and maintaining developer flow.
- Trustworthy: Because they are written first and verify behavior, passing tests should instil confidence that the system is working correctly and can be deployed. If tests pass but you don't trust the system works, it indicates a fundamental problem with how the tests were written or how TDD is being applied.
- Test Holistically (Where Appropriate): Tests should cover the relevant layers of the application stack involved in delivering the behavior, including configuration, dependency injection, routing, serialization, and interaction with downstream systems (like databases or caches), where these interactions are part of the observable outcome or required by the business. This is achieved by testing from the outside boundary (e.g., API endpoint) inwards.
- Use Mocks/Stubs Judiciously: Substitutes like mocks or stubs should primarily be used to facilitate fast, independent test runs by replacing slow or fragile dependencies (like I/O or network calls), not primarily to isolate every single class for testing in isolation.
- Incorporate Observability (for non-externally visible aspects): For certain internal states or behaviors that are not directly visible in the standard output but are important requirements (e.g., hitting a cache for performance, writing to an audit log), observability data (like tracing) can be captured and asserted upon within tests. This treats telemetry as another output or feedback loop.

# Build & Dependencies (Maven)

- All dependencies MUST be managed through the `pom.xml` file.
- Static analysis tools (`ktlint`) MUST be integrated into the Maven build lifecycle and configured to fail the build on violations.
- Part of the tcr cycle is a mvn clean check which:
  1. autofixes ktlint issues that are autofixable
  2. compiles the code
  3. runs the tests
  4. checks for other ktlint violations

# Development process and interaction with me
- @/.claude/docs/development-process.md

# Specific Technology Documentation Links
# (Claude, you MUST consult these for detailed guidance on the respective technologies)

- @/.claude/docs/kotlin-effective-style.md