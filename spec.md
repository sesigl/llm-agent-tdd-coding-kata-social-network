# Social Network Kata Project Plan

## Overview

A Test-Driven Development kata implementing a social networking application in Kotlin. The project focuses on applying strict TDD principles through Test-Commit-Revert (TCR) methodology to build a social network with core features like posting, reading timelines, following users, mentions, links, and direct messaging.

The primary functional requirements are:
1. Posting: Users can publish messages to personal timelines
2. Reading: Users can view other users' timelines
3. Following: Users can subscribe to and view aggregated timelines of other users
4. Mentions: Users can mention others in messages using "@"
5. Links: Users can include clickable web resources in messages
6. Direct Messages: Users can send private messages to other users

## Key Technical Aspects

- **Language**: Kotlin 2.1.21
- **Build Tool**: Maven
- **Testing Framework**: JUnit 5 with Kotlin test extensions
- **Development Methodology**: Test-Driven Development (TDD) with Test-Commit-Revert (TCR)
  - Tests are written before implementation
  - Code is immediately committed if tests pass
  - Code is automatically reverted if tests fail
  - Integration with Gemini API for commit message generation
- **Data Management**: In-memory approach for data storage (no persistence requirements)
- **Code Quality**: ktlint integrated for consistent code style
- **Modularity**: Clean separation of concerns following Domain-Driven Design principles

## Development Steps

1. **Posting Feature Implementation**
   - Create `TimelineService` with ability to publish messages
   - Support associating messages with users
   - Store messages with timestamps
   - Key Acceptance Criteria: Users can post messages to their personal timeline

2. **Reading Feature Implementation**
   - Extend `TimelineService` to retrieve messages for a specific user
   - Return messages in chronological order (newest first)
   - Key Acceptance Criteria: Users can view another user's timeline

3. **Following Feature Implementation**
   - Add subscription/following functionality to `TimelineService`
   - Implement aggregated timeline view for followed users
   - Key Acceptance Criteria: Users can follow others and see all followed users' posts in a single timeline

4. **Mentions Feature Implementation**
   - Add support for detecting "@username" patterns in messages
   - Create notification or special handling for mentioned users
   - Key Acceptance Criteria: Users receive notifications when mentioned in messages

5. **Links Feature Implementation**
   - Implement URL detection in messages
   - Add support for making detected URLs clickable
   - Key Acceptance Criteria: URLs in messages are automatically detected and made clickable

6. **Direct Messages Feature Implementation**
   - Extend `TimelineService` with private messaging functionality
   - Implement visibility controls (messages only visible to sender and recipient)
   - Key Acceptance Criteria: Users can send messages that only specific recipients can view

## Project Workflow & TDD with TCR Interaction

### Setup/Bootstrap
- Project setup is already complete with Maven configuration
- Basic structure with empty `TimelineService` class and test class is provided

### Iterative Development Cycle
1. **Write Failing Test**: Write a test for the next smallest increment of functionality
   - Start with basic test cases and build up complexity
   - Tests must follow established TDD patterns

2. **Implement Minimal Solution**: Write just enough code to make the failing test pass
   - Focus on simplicity and correctness, not optimization
   - Follow clean code and Kotlin best practices

3. **Execute TCR Cycle**: Run `./tcr.sh -c` 
   - Tests and linting are automatically run
   - If tests pass: Changes are automatically committed with an AI-generated summary
   - If tests fail: Changes are automatically reverted to last working state

4. **Repeat**: Continue with the next increment of functionality

### Refactoring
- Continuously refactor code while ensuring tests remain green
- Maintain clean code principles and follow Kotlin best practices
- Respect Domain-Driven Design principles when organizing code

## Hand-Off Notes

- **Language/Tools**: Kotlin 2.1.21 with JUnit 5, running on Java 21
- **Environment**: Maven 3.x with configured ktlint plugin
- **Primary Focus**: 
  - Following TDD principles and TCR workflow
  - Implementing features incrementally
  - Maintaining clean, idiomatic Kotlin code
  - Focusing on behavior testing over implementation testing
- **Git Practices**: 
  - TCR script manages commits automatically
  - Commits are only created when tests pass
  - Failed tests trigger automatic reverts
  - Use `./tcr.sh -c` to trigger an automated TCR cycle for agent-driven development