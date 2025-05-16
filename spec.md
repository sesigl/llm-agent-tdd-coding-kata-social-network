# Project Plan: Social Network Coding Kata (Kotlin, TCR)

## Overview

This project aims to implement a simple social network application incrementally, following the Test-Commit-Revert (TCR) methodology. The core functionalities to be developed are:

1.  **Posting**: Alice can publish messages to her personal timeline.
2.  **Reading**: Bob can view Alice’s timeline.
3.  **Following**: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions.
4.  **Mentions**: Bob can link to Charlie in a message using “@”.
5.  **Links**: Alice can link to a clickable web resource in a message.
6.  **Direct Messages**: Mallory can send a private message to Alice, which is a timeline entry only visible to Alice.

## Key Technical Aspects

* **Language**: Kotlin (version 1.9.23, JVM Target 21)
* **Build Tool**: Apache Maven ( Surefire 3.2.5, Failsafe 3.2.5)
* **Testing Framework**: JUnit 5
* **Development Methodology**: Test-Commit-Revert (TCR).
    * The `tcr.sh` script automates the cycle:
        * `mvn clean test` is run.
        * If tests pass, changes are staged (`git add -A`). If there are changes, a commit message is generated using the Gemini API by summarizing the diff, prefixed with `[TCR]`, and then committed.
        * If tests fail, changes are reverted (`git reset --hard HEAD`). An empty commit with a message prefixed `[TCR RESET]` (also summarized by Gemini based on the diff and Maven output) is made to log the revert.
    * The script supports an interactive mode (waiting for Enter to run a cycle or 'f' to just run tests) and a single cycle mode (`./tcr.sh -c`) which is suitable for agentic workflows.
* **Data Management**: Initially, an in-memory approach for storing timelines, messages, and user data will be sufficient.
* **Modularity**: Strive for a clean separation of concerns, potentially with distinct classes or modules for timeline management, user interactions, and message parsing. The initial code provides a `TimelineService` and `TimelineServiceTest`.

## Development Steps

The development will proceed by tackling the requirements one by one, ensuring each step is covered by tests before committing.

1.  **Posting: Alice can publish messages to her personal timeline**
    * Define a `Message` data structure (e.g., user, content, timestamp).
    * Implement functionality in `TimelineService` to allow a user (e.g., "Alice") to post a message.
    * Store the message in an in-memory collection associated with Alice.
    * Acceptance Criteria:
        * Alice can post a message.
        * The message appears on her timeline.

2.  **Reading: Bob can view Alice’s timeline**
    * Implement functionality in `TimelineService` for a user (e.g., "Bob") to retrieve another user's (e.g., "Alice") timeline.
    * The timeline should return messages in a sensible order (e.g., reverse chronological).
    * Acceptance Criteria:
        * Bob can request Alice's timeline.
        * Bob receives a list of Alice's messages.

3.  **Following: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions**
    * Implement a subscription mechanism in `TimelineService` where a user (e.g., "Charlie") can follow other users (e.g., "Alice", "Bob").
    * Implement functionality for Charlie to view a "wall" or aggregated timeline that includes messages from all users he follows, ordered appropriately.
    * Acceptance Criteria:
        * Charlie can follow Alice.
        * Charlie can follow Bob.
        * Charlie's aggregated timeline shows messages from Alice and Bob.

4.  **Mentions: Bob can link to Charlie in a message using “@”**
    * Enhance message posting to recognize "@username" patterns.
    * This might involve parsing message content. For now, the focus is on recognizing the pattern; actual notification/linking can be a future enhancement.
    * Acceptance Criteria:
        * Bob posts a message like "Hello @Charlie!".
        * The system (or tests) can identify that Charlie was mentioned.

5.  **Links: Alice can link to a clickable web resource in a message**
    * Enhance message posting to recognize URLs (e.g., "http://example.com", "https://example.com").
    * This involves parsing message content to identify links.
    * Acceptance Criteria:
        * Alice posts a message containing "Check this: http://example.com".
        * The system (or tests) can identify the URL as a link.

6.  **Direct Messages: Mallory can send a private message to Alice, which is a timeline entry only visible to Alice**
    * Implement a new type of message or a flag for "direct message".
    * Ensure that when Mallory sends a DM to Alice, only Alice can see it on her timeline/DM view. Other users, including Mallory (on Alice's public timeline), should not see it.
    * Acceptance Criteria:
        * Mallory sends a DM to Alice.
        * Alice can view the DM.
        * Bob (or any other user) cannot view Alice's DM from Mallory.

## Project Workflow & TCR Interaction

* **Setup/Bootstrap**:
    * Ensure Java 21 SDK is installed (as per `.sdkmanrc` and `pom.xml`).
    * The `GEMINI_API_KEY` environment variable must be set for the `tcr.sh` script to function correctly for commit message generation.
    * Project dependencies are managed by Maven (`pom.xml`).

* **Iterative Development Cycle (TCR)**:
    1.  **Write a Test**: Create a new test case or modify an existing one in a `*Test.kt` file (e.g., `TimelineServiceTest.kt`) that defines the next small piece of behavior for the upcoming feature.
    2.  **Check Test Failure (Optional but Recommended)**: Run `./tcr.sh -c` (or press 'f' then Enter if in interactive mode of `tcr.sh`). The build should fail because the implementation is missing. This step ensures the test is valid. The script will revert any new test code if it fails (which it should at this stage if only the test is written). A more robust approach is to write the failing test, then run `mvn clean test` manually or via the 'f' option in `tcr.sh` to see it fail without auto-revert.
    3.  **Implement the Feature**: Write the minimal amount of code in the source files (e.g., `TimelineService.kt`) to make the test pass.
    4.  **Run TCR Cycle**: Execute `./tcr.sh -c` (or press Enter in interactive mode).
        * If tests pass: Changes are automatically committed by `tcr.sh` with a Gemini-generated message.
        * If tests fail: Changes are automatically reverted by `tcr.sh`, and an empty commit logging the reset (with a Gemini-generated message) is made. The developer needs to re-evaluate the implementation or the test.
    5.  **Repeat**: Go back to step 1 for the next piece of functionality or refactoring.

* **Refactoring**:
    * Refactor code as needed to maintain clarity, improve design, and remove duplication.
    * All refactoring steps must also follow the TCR cycle: ensure tests pass before and after the refactoring.

## Hand-Off Notes

* **Language/Tools**: The project uses Kotlin 1.9.23 with Maven for building and JUnit 5 for testing. Java 21 is the target JDK.
* **Environment Assumptions**:
    * A Java 21 JDK must be available.
    * The `GEMINI_API_KEY` environment variable must be set for the `tcr.sh` script's commit message generation feature.
    * `mvn` command must be in the system PATH.
* **Primary Focus**: Strict adherence to the TCR methodology is paramount. All code changes (features, bug fixes, refactoring) must go through the `./tcr.sh -c` cycle (or interactive `tcr.sh` equivalent).
* **Git Practices**: All commits will be managed by the `tcr.sh` script. Commit messages will be automatically generated by Gemini based on the diff content, prefixed with `[TCR]` for successful changes or `[TCR RESET]` for reverts. This provides traceability and a consistent commit history. Avoid manual commits to the development branch unless for exceptional administrative reasons.