# Social Network Kata Project Implementation Plan

This document outlines a structured approach to implementing the Social Network Kata in Kotlin using TDD/TCR methodologies. Each phase is broken down into specific coding tasks with clear deliverables and implementation guidance.

## Project Overview

A social networking application built in Kotlin that simulates basic social media functionality, including posting messages, viewing timelines, following users, mentioning users, sharing links, and sending private messages. The implementation follows Test-Driven Development with Test-Commit-Revert discipline.

## High-Level Phases

1. **Core Domain Model & Posting Feature**
2. **Timeline Reading Feature**
3. **Following & Aggregated Timeline Feature**
4. **Mentions Feature**
5. **Links in Messages Feature**
6. **Private Messaging Feature**

## Phase Breakdown & Implementation Tasks

### Phase 1: Core Domain Model & Posting Feature

#### Task 1.1: Initial Domain Models

**Prompt:**
```
Implement the core domain models for the Social Network application using Kotlin data classes.

Goal: Create the foundational domain models that represent users and messages in the system.

Deliverables:
- Create a User value object in src/main/kotlin/org/codingkata/socialnetwork/domain/User.kt
- Create a Message value object in src/main/kotlin/org/codingkata/socialnetwork/domain/Message.kt
- Create a TimelineEntry value object in src/main/kotlin/org/codingkata/socialnetwork/domain/TimelineEntry.kt

Technology: Kotlin 2.1.21, following Domain-Driven Design principles.

Implementation Guidelines:
- Use Kotlin data classes to implement immutable value objects
- User should contain a username (String) property
- Message should contain content (String) and author (User) properties
- TimelineEntry should combine Message with a timestamp (java.time.Instant)
- Follow value object best practices (immutability, equality by content)
- Implement appropriate factory methods for creating instances
- Follow the TDD approach by writing tests first in parallel test classes
```

#### Task 1.2: Timeline Repository Interface

**Prompt:**
```
Create the repository interface for storing and retrieving timeline entries.

Goal: Define the contract for persisting and retrieving user timeline entries.

Deliverables:
- Create TimelineRepository interface in src/main/kotlin/org/codingkata/socialnetwork/repository/TimelineRepository.kt

Technology: Kotlin 2.1.21, Domain-Driven Design repository pattern.

Implementation Guidelines:
- Define methods for saving a message to a user's timeline
- Define methods for retrieving a user's timeline entries
- Use domain objects from the previous task
- Apply repository pattern best practices
- Ensure the interface is testable
- Follow the TDD approach by writing tests first
```

#### Task 1.3: In-Memory Timeline Repository Implementation

**Prompt:**
```
Implement an in-memory version of the TimelineRepository interface.

Goal: Create a concrete implementation of the TimelineRepository that stores data in memory.

Deliverables:
- Create InMemoryTimelineRepository in src/main/kotlin/org/codingkata/socialnetwork/repository/InMemoryTimelineRepository.kt
- Implement TimelineRepository interface from the previous task

Technology: Kotlin 2.1.21, immutable collections.

Implementation Guidelines:
- Use thread-safe immutable collections (or appropriate concurrent collections)
- Implement methods defined in the TimelineRepository interface
- Ensure proper handling of edge cases (new users, empty timelines)
- Store timeline entries by username for efficient retrieval
- Maintain chronological ordering of timeline entries
- Follow Kotlin best practices for immutability
- Follow the TDD approach by writing tests first
```

#### Task 1.4: Timeline Service Basic Implementation

**Prompt:**
```
Create the TimelineService with message posting functionality.

Goal: Implement the core service that allows users to post messages to their timelines.

Deliverables:
- Create TimelineService in src/main/kotlin/org/codingkata/socialnetwork/TimelineService.kt
- Implement message posting functionality

Technology: Kotlin 2.1.21, Domain-Driven Design service layer.

Implementation Guidelines:
- Inject TimelineRepository as a dependency
- Implement a postMessage method that accepts a username and message content
- Create domain objects (User, Message, TimelineEntry) and persist via repository
- Handle validation and edge cases
- Ensure thread safety through immutability
- Follow the TDD approach by writing tests first in TimelineServiceTest.kt
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 1.5: Refactoring - Rich Domain Models

**Prompt:**
```
Refactor the domain models to add behavior and ensure domain invariants.

Goal: Enhance the domain models with rich behavior and validation to maintain domain integrity.

Deliverables:
- Refine existing domain classes with additional behavior and validation

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Add validation logic to ensure usernames follow required format
- Add validation for message content (e.g., not empty, within character limits)
- Move appropriate business logic from service to domain objects
- Implement domain-specific factory methods for creating valid instances
- Apply value object pattern consistently
- Maintain immutability and thread safety
- Follow the TDD approach for refactoring
- Use small, incremental steps with TCR
```

### Phase 2: Timeline Reading Feature

#### Task 2.1: Timeline Reading Implementation

**Prompt:**
```
Extend the TimelineService to support reading user timelines.

Goal: Implement functionality to retrieve a user's timeline ordered by timestamp (newest first).

Deliverables:
- Update TimelineService with timeline reading functionality
- Update TimelineServiceTest with new test cases

Technology: Kotlin 2.1.21, immutable collections.

Implementation Guidelines:
- Add a method to retrieve a user's timeline entries
- Return timeline entries in reverse chronological order (newest first)
- Handle edge cases (non-existent users, empty timelines)
- Maintain immutability throughout the implementation
- Use appropriate collection operations for sorting and filtering
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 2.2: Timeline Formatting

**Prompt:**
```
Add formatting capability to the timeline entries for display purposes.

Goal: Enhance the domain model or service layer to format timeline entries in a human-readable way.

Deliverables:
- Create a TimelineFormatter in src/main/kotlin/org/codingkata/socialnetwork/TimelineFormatter.kt
- Update relevant tests

Technology: Kotlin 2.1.21, Java time API.

Implementation Guidelines:
- Implement formatting of timestamps as relative time (e.g., "5 minutes ago")
- Format timeline entries as complete messages ready for display
- Use extension functions for clean implementation
- Maintain immutability and pure functions
- Consider internationalization aspects
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 2.3: Refactoring - Separation of Concerns

**Prompt:**
```
Refactor the service layer to ensure proper separation of concerns.

Goal: Ensure clear boundaries between repository, domain, and service layers.

Deliverables:
- Refine existing classes to improve separation of concerns

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Review responsibility allocation between classes
- Move formatting logic to appropriate components
- Ensure service layer focuses on orchestration, not business rules
- Apply appropriate design patterns to improve structure
- Consider introducing Command/Query Segregation
- Maintain backward compatibility with existing tests
- Follow the TDD approach for refactoring
- Use small, incremental steps with TCR
```

### Phase 3: Following & Aggregated Timeline Feature

#### Task 3.1: Following Relationship Model

**Prompt:**
```
Create the domain model for following relationships between users.

Goal: Implement domain models to represent user following relationships.

Deliverables:
- Create FollowRelationship value object in src/main/kotlin/org/codingkata/socialnetwork/domain/FollowRelationship.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Create an immutable value object representing a following relationship
- Include properties for follower and followee
- Add appropriate validation and factory methods
- Consider using appropriate value objects rather than primitive types
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 3.2: Following Repository

**Prompt:**
```
Create the repository for storing and retrieving following relationships.

Goal: Implement a repository for managing follow relationships between users.

Deliverables:
- Create FollowRepository interface in src/main/kotlin/org/codingkata/socialnetwork/repository/FollowRepository.kt
- Implement InMemoryFollowRepository in src/main/kotlin/org/codingkata/socialnetwork/repository/InMemoryFollowRepository.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, Domain-Driven Design repository pattern.

Implementation Guidelines:
- Define methods for creating/removing follow relationships
- Define methods for retrieving followers and followees
- Implement an in-memory version using thread-safe collections
- Apply repository pattern best practices
- Ensure proper handling of edge cases
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 3.3: Following Service Implementation

**Prompt:**
```
Extend the TimelineService to support following users and viewing aggregated timelines.

Goal: Implement functionality to follow other users and view their combined timelines.

Deliverables:
- Update TimelineService with following functionality
- Update TimelineService with aggregated timeline functionality
- Update tests with new cases

Technology: Kotlin 2.1.21, functional programming concepts.

Implementation Guidelines:
- Add a method to follow another user
- Add a method to retrieve an aggregated timeline from all followed users
- Merge multiple timelines while maintaining timestamp ordering
- Handle edge cases (no followed users, etc.)
- Use functional programming techniques for timeline merging
- Maintain immutability throughout the implementation
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 3.4: Refactoring - Domain Events

**Prompt:**
```
Refactor the implementation to use domain events for communicating between components.

Goal: Improve the design by introducing domain events for important actions.

Deliverables:
- Create a domain event system in src/main/kotlin/org/codingkata/socialnetwork/event/
- Refactor services to use domain events

Technology: Kotlin 2.1.21, Domain-Driven Design event pattern.

Implementation Guidelines:
- Define domain events for actions like posting messages and following users
- Implement a simple event bus for publishing and subscribing to events
- Refactor services to publish events when domain actions occur
- Consider using observer pattern or a lightweight event system
- Maintain immutability and thread safety
- Ensure backward compatibility with existing tests
- Follow the TDD approach for refactoring
- Use small, incremental steps with TCR
```

### Phase 4: Mentions Feature

#### Task 4.1: Mention Detection

**Prompt:**
```
Implement mention detection in message content.

Goal: Create functionality to detect @username mentions in message content.

Deliverables:
- Create MentionDetector in src/main/kotlin/org/codingkata/socialnetwork/MentionDetector.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, regular expressions.

Implementation Guidelines:
- Implement a service or domain behavior to detect @username mentions
- Extract mentioned usernames from message content
- Handle edge cases (multiple mentions, mentions at different positions)
- Use regular expressions or string parsing techniques
- Follow functional programming principles
- Create pure functions for mention detection
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 4.2: Mention Association

**Prompt:**
```
Enhance the domain model to associate mentions with users and messages.

Goal: Extend the domain model to represent mentions and associate them with messages.

Deliverables:
- Update the Message domain object to include mentions
- Create Mention value object if needed
- Update repositories and services
- Update tests

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Extend the Message domain object to include a collection of mentioned users
- Create a Mention value object if a more complex representation is needed
- Update repositories to store mention information
- Update service layer to process mentions when messages are posted
- Consider using domain events for mention notifications
- Maintain immutability throughout
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 4.3: Mentions Timeline View

**Prompt:**
```
Implement a specialized timeline view for messages mentioning a specific user.

Goal: Create functionality to retrieve a timeline of messages that mention a specific user.

Deliverables:
- Update TimelineService with mention timeline functionality
- Create appropriate tests

Technology: Kotlin 2.1.21, functional programming concepts.

Implementation Guidelines:
- Add a method to retrieve messages mentioning a specific user
- Return results in chronological order
- Apply appropriate filtering techniques
- Consider efficient retrieval strategies
- Maintain immutability throughout the implementation
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 4.4: Refactoring - Rich Domain Models

**Prompt:**
```
Refactor the domain model to make mentions a first-class concept.

Goal: Enhance the domain model to better represent mentions as a core domain concept.

Deliverables:
- Refine domain models to better represent mentions
- Update services and repositories accordingly
- Update tests

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Review mention representation in the domain model
- Consider creating specialized aggregates for mentions if appropriate
- Ensure domain invariants are enforced
- Apply rich domain model principles
- Maintain backward compatibility with existing tests
- Follow the TDD approach for refactoring
- Use small, incremental steps with TCR
```

### Phase 5: Links in Messages Feature

#### Task 5.1: Link Detection

**Prompt:**
```
Implement link detection in message content.

Goal: Create functionality to detect and validate URLs in message content.

Deliverables:
- Create LinkDetector in src/main/kotlin/org/codingkata/socialnetwork/LinkDetector.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, regular expressions, URL validation.

Implementation Guidelines:
- Implement a service or domain behavior to detect URLs in messages
- Support common URL formats (http, https, etc.)
- Validate detected URLs for basic formatting
- Use regular expressions or dedicated URL parsing libraries
- Create pure functions for link detection
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 5.2: Link Representation

**Prompt:**
```
Enhance the domain model to represent links in messages.

Goal: Extend the domain model to properly represent links found in messages.

Deliverables:
- Update the Message domain object to include links
- Create Link value object in src/main/kotlin/org/codingkata/socialnetwork/domain/Link.kt
- Update repositories and services
- Update tests

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Create a Link value object with properties like URL and display text
- Extend the Message domain object to include a collection of links
- Update repositories to store link information
- Update service layer to process links when messages are posted
- Maintain immutability throughout
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 5.3: Link Rendering

**Prompt:**
```
Implement link formatting for display in timeline views.

Goal: Enhance the timeline formatting to properly display clickable links.

Deliverables:
- Update TimelineFormatter to handle links
- Update tests with link formatting cases

Technology: Kotlin 2.1.21, text formatting.

Implementation Guidelines:
- Enhance the formatting logic to render links in a clickable format
- Consider different output formats (plain text, HTML, etc.)
- Handle edge cases (multiple links, links with special characters)
- Maintain clean separation of concerns
- Use extension functions for clean implementation
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 5.4: Refactoring - Message Content Processing

**Prompt:**
```
Refactor message content processing into a unified pipeline.

Goal: Improve the design by creating a consistent processing pipeline for message content.

Deliverables:
- Create a MessageContentProcessor in src/main/kotlin/org/codingkata/socialnetwork/MessageContentProcessor.kt
- Refactor mention and link detection into this pipeline
- Update tests

Technology: Kotlin 2.1.21, functional programming, pipeline pattern.

Implementation Guidelines:
- Implement a processing pipeline for message content
- Integrate both mention and link detection into this pipeline
- Use functional composition techniques
- Consider using the Chain of Responsibility pattern
- Ensure extensibility for future content processing needs
- Maintain backward compatibility with existing tests
- Follow the TDD approach for refactoring
- Use small, incremental steps with TCR
```

### Phase 6: Private Messaging Feature

#### Task 6.1: Private Message Domain Model

**Prompt:**
```
Create the domain model for private messages.

Goal: Implement domain models to represent private messages between users.

Deliverables:
- Create PrivateMessage value object in src/main/kotlin/org/codingkata/socialnetwork/domain/PrivateMessage.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Create an immutable value object representing a private message
- Include properties for sender, recipient(s), content, and timestamp
- Support multiple recipients if appropriate
- Include appropriate validation and factory methods
- Consider reusing existing message content processing
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 6.2: Private Message Repository

**Prompt:**
```
Create the repository for storing and retrieving private messages.

Goal: Implement a repository for managing private messages between users.

Deliverables:
- Create PrivateMessageRepository interface in src/main/kotlin/org/codingkata/socialnetwork/repository/PrivateMessageRepository.kt
- Implement InMemoryPrivateMessageRepository in src/main/kotlin/org/codingkata/socialnetwork/repository/InMemoryPrivateMessageRepository.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, Domain-Driven Design repository pattern.

Implementation Guidelines:
- Define methods for saving private messages
- Define methods for retrieving messages for a user (sent and received)
- Implement an in-memory version using thread-safe collections
- Apply repository pattern best practices
- Ensure proper handling of privacy concerns and edge cases
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 6.3: Private Messaging Service

**Prompt:**
```
Extend the application to support private messaging functionality.

Goal: Implement service layer functionality for sending and receiving private messages.

Deliverables:
- Create PrivateMessageService in src/main/kotlin/org/codingkata/socialnetwork/PrivateMessageService.kt
- Create appropriate tests

Technology: Kotlin 2.1.21, Domain-Driven Design service layer.

Implementation Guidelines:
- Implement methods for sending private messages
- Implement methods for retrieving a user's private messages
- Apply proper access control to ensure message privacy
- Reuse message content processing for mentions and links
- Support sorting and filtering of private messages
- Maintain immutability throughout the implementation
- Follow the TDD approach by writing tests first
- Follow the TCR workflow with ./tcr.sh -c
```

#### Task 6.4: Refactoring - Unified Message Abstraction

**Prompt:**
```
Refactor the system to unify public and private messaging concepts.

Goal: Improve the design by creating a common abstraction for different message types.

Deliverables:
- Refactor domain models to share common message concepts
- Update services and repositories
- Update tests

Technology: Kotlin 2.1.21, Domain-Driven Design principles.

Implementation Guidelines:
- Create a common message interface or abstract base class
- Refactor public and private messages to share this abstraction
- Apply proper inheritance or composition techniques
- Ensure type safety through the domain model
- Consider using sealed classes for message type hierarchies
- Maintain backward compatibility with existing tests
- Follow the TDD approach for refactoring
- Use small, incremental steps with TCR
```

## Conclusion

This prompt plan provides a structured approach to implementing the Social Network Kata using TDD/TCR methodologies in Kotlin. By following these prompts in order, you'll create a well-designed application with clean separation of concerns, rich domain models, and a maintainable codebase.

Each task builds incrementally on previous work, with dedicated refactoring steps to continuously improve the design. The TDD/TCR workflow ensures that all code is thoroughly tested and maintains backward compatibility throughout the development process.