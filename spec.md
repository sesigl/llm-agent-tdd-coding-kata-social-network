# Social Network Kata Project Plan

## Overview

This project implements a simple social network application using Kotlin. The social network will allow users to post messages, view timelines, follow others, mention users, include links, and send direct messages.

1. Create a messaging platform for users to share updates
2. Enable timeline viewing functionality
3. Implement subscription/following capabilities
4. Add user mention functionality using '@' symbol
5. Support clickable web links in messages
6. Provide private direct messaging functionality

## Key Technical Aspects

- **Language**: Kotlin 2.1.21 with Java 21
- **Build Tool**: Maven with ktlint for code style enforcement
- **Testing Framework**: JUnit 5 with Kotlin Test
- **Development Methodology**: Test-Commit-Revert (TCR), a strict form of Test-Driven Development where failing tests cause all changes to be reverted
- **Data Management**: In-memory approach for this kata, with clear domain models
- **Modularity**: Domain-driven design with clean separation of concerns

## Development Steps

1. **Posting Feature**
   - Implement a service for users to post messages to their timeline
   - Store messages with user identification and timestamp
   - Associate messages with the correct user's timeline
   - Ensure posts are retrievable by their author

2. **Reading Feature**
   - Create functionality to view a specific user's timeline
   - Display messages in chronological order (newest first)
   - Include post content, timestamp, and author information

3. **Following Feature**
   - Implement subscription functionality between users
   - Create an aggregated timeline view combining followed users' posts 
   - Maintain proper ordering of posts from multiple sources
   - Handle edge cases like self-following or following non-existent users

4. **Mentions Feature**
   - Implement @mention functionality to reference other users
   - Parse message content to identify mentions
   - Make mentions discoverable/searchable
   - Support linking to mentioned users

5. **Links Feature**
   - Support clickable web resource links within messages
   - Identify and parse URLs in message content
   - Ensure links are properly formatted and accessible
   - Handle edge cases like malformed URLs

6. **Direct Messages Feature**
   - Implement private messaging between users
   - Ensure messages are only visible to sender and recipient
   - Integrate with existing timeline infrastructure
   - Support all message features in private communications

## Project Workflow & TCR Interaction

### Setup/Bootstrap
1. Clone the repository
2. Ensure JDK 21 is installed and configured
3. Set the GEMINI_API_KEY environment variable (needed for the TCR script)
4. Run `./tcr.sh -c` for a single TCR cycle or `./tcr.sh` for interactive mode

### Iterative Development Cycle

For agentic development (AI-assisted workflow):

1. **Write a failing test** for the smallest slice of functionality
2. **Implement the minimal code** to make the test pass
3. **Run the TCR cycle** using `./tcr.sh -c`
   - If tests pass: Changes are automatically committed with an AI-generated summary
   - If tests fail: Changes are automatically reverted to the last working state
4. **Repeat** for the next increment of functionality

### Refactoring
- Refactoring should happen after tests pass and before implementing new functionality
- Follow Domain-Driven Design principles for structuring the codebase
- Place files in the correct packages following the structure defined in CLAUDE.md
- Use value objects, entities, aggregates, and repositories appropriately

## Hand-Off Notes

### Language/Tools
- Kotlin 2.1.21 with JDK 21
- Maven for building and dependency management
- ktlint for code style enforcement
- JUnit 5 for testing

### Environment Assumptions
- JDK 21 installed
- Maven installed
- GEMINI_API_KEY environment variable set for commit message generation

### Primary Focus
- Strict adherence to Test-Driven Development (TDD) using Test-Commit-Revert (TCR)
- Focus on behavior-driven tests that specify what the system should do
- Clean code following Domain-Driven Design principles
- Incremental development with small, focused commits

### Git Practices
- TCR script handles all commits and reverts automatically
- Never commit code that doesn't pass tests (TCR enforces this)
- Commit messages are generated automatically by Gemini AI
- Small, incremental changes are preferred