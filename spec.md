# Social Network Kata Project Plan - Kotlin TDD/TCR Approach

## Overview
A social networking application built in Kotlin that simulates basic social media functionality. The implementation will be driven by Test-Driven Development (TDD) with the added discipline of Test-Commit-Revert (TCR) methodology.

Key requirements:
1. Users can publish messages to their personal timelines
2. Users can view other users' timelines
3. Users can subscribe to others' timelines and view aggregated content
4. Users can mention other users with "@" syntax
5. Users can include clickable links in messages
6. Users can send private messages visible only to recipients

## Key Technical Aspects

### Language & Framework
- **Language**: Kotlin 2.1.21
- **Build Tool**: Maven
- **Testing Framework**: JUnit 5 with Kotlin test extensions
- **JVM Target**: Java 21
- **Code Style**: ktlint for automated style enforcement

### Development Methodology
- **Test-Driven Development (TDD)**: Write failing tests first, implement minimal code to pass, then refactor
- **Test-Commit-Revert (TCR)**: A stricter version of TDD where failing tests trigger automatic code reversion
- **Immutability First**: Prefer immutable data structures and `val` over `var`
- **Clean Code**: Follow Kotlin best practices for readability and maintainability

### Data Management
- **Initial Approach**: In-memory data stores with immutable data structures
- **Data Models**: Implement domain entities as Kotlin data classes
- **Thread Safety**: Implement with immutability and concurrency considerations

### Modularity
- **Package Structure**: Organized by domain feature under `org.codingkata.socialnetwork`
- **Separation of Concerns**: Clear boundaries between service interfaces and implementations
- **Service Layer**: Implement core business logic in service classes

## Development Steps

1. **Posting Feature**
   - Create `TimelineService` with message posting functionality
   - Implement user and message domain models
   - Store messages with associated usernames and timestamps
   - Test multiple post scenarios and edge cases

2. **Reading Feature**
   - Extend `TimelineService` to retrieve messages for a specific user
   - Implement chronological ordering (newest first)
   - Test timeline retrieval with various message counts and formats

3. **Following Feature**
   - Add subscription/following capability to `TimelineService`
   - Implement aggregated timeline view for subscriptions
   - Support merging multiple timelines with correct ordering
   - Test various following scenarios and edge cases

4. **Mentions Feature**
   - Extend message parsing to detect "@username" syntax
   - Add functionality to associate mentions with users
   - Test various mention formats and edge cases

5. **Links Feature**
   - Implement link detection in message content
   - Ensure links are stored in a format that maintains clickability
   - Test with various link formats and protocols

6. **Direct Messages Feature**
   - Add private messaging functionality to `TimelineService`
   - Implement visibility controls for messages
   - Test message privacy and access control

## Project Workflow & TCR Interaction

### Setup/Bootstrap
1. Project is set up with Maven and essential dependencies
2. Basic `TimelineService` and test structure is in place
3. TCR script (`tcr.sh`) is available for automating the TCR workflow

### Iterative Development Cycle
1. **Write Failing Test**: Create a test that verifies the next small increment of functionality
2. **Run TCR**: Execute `./tcr.sh -c` to perform a single TCR cycle:
   - If tests pass: Changes are automatically committed with an AI-generated summary
   - If tests fail or contain ktlint violations: Changes are automatically reverted to the last working state
3. **Repeat**: Continue with the next small increment of functionality

### Refactoring
- Refactoring should be done in small, incremental steps
- Each refactoring step should be followed by a TCR cycle
- The TCR process ensures refactoring doesn't break existing functionality

## Hand-Off Notes

### Language & Tools
- Kotlin 2.1.21 with Java 21 JVM target
- Maven for build management
- ktlint for code style enforcement
- TCR script for development workflow

### Environment
- Ensure GEMINI_API_KEY environment variable is set for TCR script operation
- Use Java 21 or compatible JDK

### Primary Focus
- Strictly follow the TDD/TCR methodology
- Implement features incrementally in the order specified
- Maintain clean code principles and Kotlin best practices
- Write behavior-focused tests (what the system does, not how it does it)

### Git Practices
- TCR script manages commits automatically
- Commits occur only when tests pass
- Failed cycles result in reversion to the last working state
- Commit messages are auto-generated with AI assistance for traceability