# Social Network Coding Kata


This repository contains different branches with correct but different solutions to compare effectiveness of AI programming.

The following branches are available:
- `tdd-only` - Pure TDD without AI
- `tdd-with-ai` TDD with AI

The comparison wont be perfect. I practiced implementing this kata before, and did it again with different limitations. This might not reflect reality.

## Hypothesis

LLMs have become very fast and powerful generating code. 

Hence, if instructions are clear they can solve problems in seconds.

The biggest limiting factor of LLMs is that the quality of code they are trained on, might be too low for what you expect. Building on "average" code
your company might be go fast in the early stages, but will slow down as the codebase grows.

This can be addressed by using TDD to provide from the beginning long-living executable guardrails. That's nothing new, but the question is if LLMs can accelerate engineers so we get the best of both worlds.

## The tooling

To make TDD more strict and comparable, a technique [test-commit-revert](https://nvoulgaris.com/test-commit-revert/) is used. This gives us a more strict ruleset how to do TDD, but also
allows us to do more analysis comparing development with and without agentic help. 

### The development cycle

TCR is a more strict TDD. I recommend the following executing cycle (which might be different how others do TCR):

Preparation:
- run `./tcr.sh` to start the TCR process

Cycle:
1. Write a test, press "f" to check if the build or test fails
2. Do the implementation
3. Press "ENTER" to execute the TCR check. If tests are green then the changes are committed. If not the changed are reset. In both cases there is an empty or not empty commit with a useful commit message leveraging Gemini for traceability.
4. Start again with Step 1

## The Kata

Source: https://kata-log.rocks/social-network-kata

This is an incremental kata to simulate a real business situation: work your way through the steps in order, but do not read the next requirement before you have finished your current one.

Your Team is tired of all those boring tasks like bowling game scores, bank accounts, singing songs or commanding mars rovers. This time you want to do something truly innovative: A Social Network!

### Backlog of requirements

- Posting: Alice can publish messages to her personal timeline
- Reading: Bob can view Alice’s timeline
- Following: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions
- Mentions: Bob can link to Charlie in a message using “@”
- Links: Alice can link to a clickable web resource in a message
- Direct Messages: Mallory can send a private message to Alice, which is a timeline entry only visible to Alice

## Evaluation

To get insights about how efficient development is, and how to evaluate the result with different metrics related to number of features covered, additional cases covered, clean code, cohesion, coupling, test coverage etc. metrics will be collected leveraging state of the art LLM.

### Metrics

- Main Use Cases Coverage between 0/6 to 6/6
- Number of additional Edge/Use Cases
- Time Until Completion in seconds
- Test-to-Code lines of code ratio 
- Number of average assertions per test
- Cyclomatic complexity counting the number of linearly independent paths through the code
- Code duplication in percentage
- Code Smells
- Behavior vs. Implementation Focus rated by the LLM between 1 (not behavior focused tests) to 5 (excellent bdd)
- Clean Code of Implementation (following best practices from Martin J. Martins and other pioneers) rated by the LLM between 1 (not clean) to 5 (excellent clean code)
- Clean Code of Tests, e.g. readability of Assertions,  (following best practices from Martin J. Martins and other pioneers) rated by the LLM between 1 (not clean) to 5 (excellent clean code)

### Evaluation Prompt

```
Analyze the following data from a 30-minute Test-Commit-Revert (TCR) coding session for the "Social Network Kata". The goal is to evaluate the development process based on the provided commit log, final source code, and final test code.

**Context:**
- The session duration was 30 minutes (1800 seconds).
- The methodology used was Test-Commit-Revert (TCR), where tests must pass for changes to be committed; otherwise, they are reverted.
- The kata involves implementing features for a simple social network (Posting, Reading, Following, Mentions, Links, Direct Messages).
- The commit log includes timestamps and messages indicating successful commits or reverts.

**Input Data:**

1.  **Commit Log (including timestamps, commit messages, and commit/revert status):**
    ```
    [PASTE COMMIT LOG DATA HERE]
    ```

2.  **Final Source Code:**
    ```[language]
    [PASTE FINAL SOURCE CODE HERE]
    ```

3.  **Final Test Code:**
    ```[language]
    [PASTE FINAL TEST CODE HERE]
    ```

**Analysis Task:**

Based *only* on the provided data, calculate the following metrics and provide a general summary of the session. Adhere strictly to the definitions provided below.

**Metric Definitions:**

1.  `main_use_cases_coverage`: Integer (0-6). Count how many of the 6 main requirements (Posting, Reading, Following, Mentions, Links, Direct Messages) appear to be implemented and tested according to the final code and tests.
2.  `additional_edge_cases`: Integer. Count the number of distinct edge cases or alternative scenarios explicitly handled in the tests beyond the most basic path for each implemented main requirement.
3.  `time_to_completion_or_session_end_seconds`: Integer. Report 1800 (the session duration) as the kata was likely not fully completed in 30 mins. If, exceptionally, all 6 use cases were verifiably completed *before* the 30min mark according to the commit log, estimate the time based on the last relevant commit timestamp. Otherwise, always use 1800.
4.  `test_to_code_ratio`: Float. Calculate the ratio of lines of code in the final test files to the lines of code in the final source files (excluding comments and blank lines if possible, otherwise total lines). State the method used (e.g., total lines or non-comment/blank lines).
5.  `average_assertions_per_test`: Float. Calculate the average number of assertion statements per test method/function found in the final test code.
6.  `cyclomatic_complexity`: Integer. Estimate the *average* cyclomatic complexity across the functions/methods in the *final source code*. If possible, state the tool/methodology used for estimation (e.g., counting decision points + 1).
7.  `code_duplication_percentage`: Float. Estimate the percentage of duplicated code within the *final source code*. Briefly state the basis for this estimation (e.g., visual inspection, assumed tooling).
8.  `code_smells`: List of strings. Identify potential code smells present in the *final source code* based on common definitions (e.g., Long Method, Large Class, Feature Envy, Duplicated Code, etc.). List the identified smells.
9.  `test_focus_rating`: Integer (1-5). Rate the tests based on their focus on *behavior* (what the system should do) versus *implementation details* (how it does it). 1 = Heavily implementation-focused, difficult to understand behavior; 5 = Clearly specifies behavior (BDD-like), independent of implementation details.
10. `implementation_clean_code_rating`: Integer (1-5). Rate the *final source code* based on general Clean Code principles (e.g., meaningful names, small functions, SRP, comments quality, formatting). 1 = Poor adherence; 5 = Excellent adherence.
11. `test_clean_code_rating`: Integer (1-5). Rate the *final test code* based on general Clean Code principles (e.g., readability, structure like Arrange-Act-Assert, clear assertion messages, meaningful test names). 1 = Poor adherence; 5 = Excellent adherence.
12. `ai_support_level`: "none", "completion", "edit" or "agent". The amount of AI support in the task.
13. `day`: Integer 0-30. The number of times I have done the tasks with different level of AI support.
14. `description`: Overall description of the test run.

**Output Format:**

Provide the results in two parts:
1.  A JSON object containing the calculated metrics.
2.  A general summary section (plain text).

**Example JSON Structure:**
```json
{
  "ai_support_level": "completion",
  "day": 2,
  "main_use_cases_coverage": 3,
  "additional_edge_cases": 2,
  "time_to_completion_or_session_end_seconds": 1800,
  "test_to_code_ratio": 1.8,
  "average_assertions_per_test": 1.5,
  "cyclomatic_complexity": 2,
  "code_duplication_percentage": 5.0,
  "code_smells": ["Duplicated Code", "Primitive Obsession"],
  "test_focus_rating": 4,
  "implementation_clean_code_rating": 3,
  "test_clean_code_rating": 4
}
```

