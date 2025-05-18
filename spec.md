# Social Network Coding Kata - Project Plan

## Overview

A Kotlin-based implementation of a social network with core social media functionality, following Test-Driven Development (TDD) and Test-Commit-Revert (TCR) methodologies. The project aims to incrementally build social network features through strict adherence to TDD principles.

1. Allow users to publish messages to personal timelines
2. Enable users to view other users' timelines
3. Implement subscription functionality for aggregated timeline views
4. Support tagging other users with @ mentions
5. Enable linking to web resources in messages
6. Support private direct messaging between users

## Key Technical Aspects

- **Language**: Kotlin 2.1.21 targeting JVM 21
- **Build Tool**: Maven with configured Kotlin and ktlint plugins
- **Testing Framework**: JUnit 5 with Kotlin test extensions
- **Development Methodology**: Test-Driven Development (TDD) with Test-Commit-Revert (TCR)
  - Write tests first, implement minimal code to pass, then refactor
  - TCR enforces full test passing for each commit, with automatic revert on failures
- **Data Management**: In-memory data structures initially (can be evolved later)
- **Modularity**: Clean separation of concerns with interfaces and implementation classes

## Development Steps

1. **Timeline Publishing (Posting Feature)**
   - Implement `TimelineService` to handle message posting
   - Enable associating messages with users
   - Store messages with timestamps
   - Acceptance: Users can post messages that are stored in their timelines

2. **Timeline Reading**
   - Implement retrieval of messages for a specific user
   - Display messages in chronological order
   - Acceptance: Any user can view another user's timeline

3. **Following/Subscription Feature**
   - Implement subscription mechanism to follow other users
   - Create aggregated timeline view across all subscriptions
   - Acceptance: Users can view a combined feed of all followed users' posts

4. **@ Mentions Implementation**
   - Add parsing logic to detect @ mentions in messages
   - Create notification or highlighting system for mentions
   - Acceptance: Users can mention others with @ and the system recognizes it

5. **Web Links in Messages**
   - Implement URL detection and validation in message content
   - Make links clickable/interactive in the timeline view
   - Acceptance: URLs in messages are properly formatted as clickable links

6. **Direct/Private Messaging**
   - Implement private message functionality
   - Set visibility controls to limit access to message sender and recipient
   - Acceptance: Users can send messages that only the intended recipient can view

## Project Workflow & TCR Interaction

### Setup/Bootstrap
- Initial project structure is provided with minimal `TimelineService` and `TimelineServiceTest` classes
- Maven configuration with Kotlin and ktlint plugins is pre-configured

### Iterative Development Cycle (TCR)

For agentic development:
1. Write a failing test for a specific feature increment
2. Implement minimal code to make the test pass
3. Run TCR cycle using: `./tcr.sh -c`
   - This executes a single TCR cycle without requiring user interaction
   - If tests pass: Changes are automatically committed with an AI-generated summary
   - If tests fail or ktlint violations exist: Changes are automatically reverted
4. Continue to the next feature increment

For manual development:
1. Run `./tcr.sh` (without flags) to start an interactive TCR session
2. Write a failing test and press "f" to check build/test failure
3. Implement code to make the test pass
4. Press ENTER to run the TCR check
   - Success: Changes are committed with a message
   - Failure: Changes are reverted automatically

### Refactoring
- After implementing a feature, consider refactoring while maintaining passing tests
- Always ensure ktlint rules are followed for code style
- Use TCR cycles for refactoring to ensure tests remain green

## Hand-Off Notes

### Language/Tools
- Kotlin 2.1.21 with JVM target 21
- Maven for build and dependency management
- ktlint for code style enforcement (integrated with Maven lifecycle)
- JUnit 5 for testing

### Environment Assumptions
- Only standard Kotlin/JVM libraries are needed initially
- No external persistence or UI frameworks required to start

### Primary Focus
- TDD approach is mandatory: tests must be written before implementation
- TCR process ensures discipline in the TDD workflow
- Passing ktlint checks is required for successful commits

### Git Practices
- The TCR script manages all Git interactions (commits/reverts)
- Commits are automatically generated when tests pass
- Failed tests trigger automatic reverts
- For agentic development, always use the `-c` flag with tcr.sh