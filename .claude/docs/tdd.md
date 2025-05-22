# Core Testing Philosophy

You are expected to follow Test-Driven Development (+TCR) principles for all coding tasks. This means writing tests *before* writing implementation code.

## Primary Testing Strategy: Outside-In TDD

**This is your default and preferred approach.**

1.  **Start with Behavior:** For any new feature or change, begin by writing tests at the **application service level**. These tests should define *what* the system should do from an external perspective (e.g., API endpoint, user interaction flow). Think in terms of BDD (Behavior-Driven Development) – describe the scenario, the action, and the expected outcome.
    * *Example:* "When a `POST` request is made to `/users` with valid data, a new user should be created, and a `201 Created` response with the user's ID should be returned."
2.  **Implement to Pass:** Write the necessary code across application services, domain logic, and infrastructure collaborators to make these high-level tests pass. Write the *minimum* amount of code required.
3.  **Refactor Internals:** Once the application service level tests are green, you can refactor the internal implementation (e.g., domain services, entities, helper functions) for better design, clarity, and efficiency. The high-level tests will act as your safety net, ensuring the overall behavior remains correct.

**Benefits I Expect from this Approach:**

* **Focus on Requirements:** Ensures code directly addresses the feature's purpose.
* **Robust Refactoring:** Allows internal code structure to change without breaking tests that define the actual observable behavior.
* **Minimal Test Overlap:** Avoids redundant tests at lower levels if the behavior is already covered at the service level.
* **Clear Specifications:** Tests serve as executable documentation of how the application should behave.

## Secondary Testing Strategy: Inside-Out TDD (Unit Tests)

**Use this approach sparingly and only when necessary.**

You should only write granular unit tests (testing a single class or function in isolation) when:

1.  **Complex, Isolated Logic:** The specific algorithm or business rule within a single unit is highly complex and cannot be easily or thoroughly tested via the application service layer without overly complicated test setups.
2.  **Critical Core Components:** The unit represents a foundational piece of the domain model whose individual correctness is absolutely critical and benefits from highly focused, isolated tests.
3.  **Difficult to Cover Otherwise:** Specific edge cases or nuanced behaviors of a small, internal component are impractical or impossible to meaningfully trigger and assert through the broader application service tests.

**If you opt for unit tests:**

* Clearly identify the isolated unit.
* Write focused tests for that unit, mocking its external dependencies.
* Implement the unit's logic to pass these tests.

**Justification Required:** If you predominantly use unit tests for a feature or deviate significantly from the outside-in approach, please provide a brief justification in your reply explaining why the service-level testing was not suitable for the core logic being implemented.
