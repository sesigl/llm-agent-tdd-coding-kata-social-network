# Social Network Application - Prompt Plan

This document outlines a comprehensive plan for implementing a social networking application in Kotlin using Test-Driven Development (TDD) with Test-Commit-Revert (TCR) methodology. The plan is broken down into distinct phases, each with specific coding tasks and implementation details.

## Project Overview

The social network application will enable users to post messages, read timelines, follow other users, mention users in posts, include clickable links, and send direct messages. The implementation will use an in-memory approach for data storage and follow Domain-Driven Design principles.

**Key Technologies:**
- Language: Kotlin 2.1.21
- Build Tool: Maven
- Testing Framework: JUnit 5 with Kotlin extensions
- Code Quality: ktlint for consistent code style
- Development Methodology: TDD with TCR

## High-Level Phases

1. **Domain Model Foundation**
   - Define core domain models
   - Implement basic timeline service

2. **Core Social Features**
   - Posting messages
   - Reading timelines
   - Following users

3. **Enhanced Social Interactions**
   - Mentions functionality
   - Link detection and handling
   - Direct messaging

4. **Refactoring and Optimization**
   - Improve domain model
   - Enhance service architecture
   - Optimize for performance and maintainability

## Detailed Implementation Plan

### Phase 1: Domain Model Foundation

#### Task 1.1: Create Core Domain Models

**Prompt:**
```
Implement the core domain model for the social networking application in Kotlin.

Specific Deliverables:
1. A Value Object `Message` that represents a social media post with:
   - Content (text)
   - Author (username)
   - Timestamp (creation time)
   - Methods for checking message properties

2. A Value Object `User` that represents a user with:
   - Username (unique identifier)
   - Methods for user-related operations

Both classes should be immutable and follow Value Object patterns from DDD.

Language: Kotlin 2.1.21
Context: These domain models will form the foundation of our social network. They should be implemented as proper Value Objects without getters/setters, using Kotlin data classes appropriately but ensuring rich domain behavior.

Start with failing tests that demonstrate the required behavior of these classes before implementing them.
```

#### Task 1.2: Implement Basic Timeline Service (Posting Feature)

**Prompt:**
```
Implement the basic TimelineService that allows users to post messages to their personal timeline.

Specific Deliverables:
1. Enhance the TimelineService class with:
   - Method to post a message to a user's timeline
   - In-memory storage for user timelines
   - Ability to record the message with its timestamp

Language: Kotlin 2.1.21
Context: The TimelineService will manage user timelines, starting with the ability to post messages. Use the domain models created in the previous task. The service should store messages in-memory with the proper association to users.

Follow TDD methodology by writing tests first that validate a user can post messages to their timeline. Then implement the minimum code necessary to make the tests pass. Run the TCR script (./tcr.sh -c) to ensure a proper commit cycle.
```

#### Task 1.3: Refactoring - Timeline as an Aggregate

**Prompt:**
```
Refactor the current implementation to introduce a Timeline as a proper aggregate root from DDD.

Specific Deliverables:
1. Create a Timeline class that:
   - Acts as an Aggregate Root
   - Contains a collection of Messages for a specific User
   - Enforces invariants (e.g., chronological ordering)
   - Provides methods to add messages and retrieve them in proper order

2. Refactor TimelineService to:
   - Manage Timeline aggregates instead of loose Messages
   - Enforce domain rules through the Timeline aggregate

Language: Kotlin 2.1.21
Context: This refactoring improves the domain model by properly encapsulating message collections within a Timeline aggregate. The Timeline becomes responsible for maintaining its internal consistency and business rules.

Ensure all existing tests continue to pass during and after the refactoring. Use the TCR script to validate that refactoring doesn't break existing functionality.
```

### Phase 2: Core Social Features

#### Task 2.1: Reading Timeline Feature

**Prompt:**
```
Implement the ability to read a user's timeline through the TimelineService.

Specific Deliverables:
1. Enhance TimelineService with:
   - Method to retrieve all messages from a specific user's timeline
   - Chronological ordering of messages (newest first)

2. Update Timeline aggregate to:
   - Support efficient retrieval of messages
   - Ensure proper message ordering

Language: Kotlin 2.1.21
Context: Users should be able to view any user's timeline, with messages displayed in reverse chronological order (newest first). The implementation should use the Timeline aggregate to enforce business rules.

Write tests first that validate reading a timeline returns the correct messages in the proper order. Then implement the minimum code necessary to make the tests pass.
```

#### Task 2.2: Following Users Feature

**Prompt:**
```
Implement functionality for users to follow other users and view an aggregated timeline.

Specific Deliverables:
1. Enhance the domain model with:
   - A Following relationship between Users
   - Methods to establish and query following relationships

2. Enhance TimelineService with:
   - Method to follow another user
   - Method to retrieve an aggregated timeline of all followed users' messages
   - Proper chronological ordering of the aggregated timeline (newest first)

Language: Kotlin 2.1.21
Context: Users should be able to follow other users and see all posts from followed users in a single timeline. This requires tracking following relationships and merging multiple timelines while maintaining chronological order.

Write tests that validate following behavior and aggregated timeline retrieval before implementing the functionality.
```

#### Task 2.3: Refactoring - Following as a Value Object

**Prompt:**
```
Refactor the following functionality to use a proper Following value object and improve the domain model.

Specific Deliverables:
1. Create a Following value object:
   - Representing a following relationship between two users
   - Immutable and properly encapsulated

2. Enhance User or create a UserFollowing entity:
   - Store following relationships using the new value object
   - Methods to manage following relationships

3. Refactor TimelineService:
   - Use the enhanced domain model for following operations
   - Improve the aggregation of timelines

Language: Kotlin 2.1.21
Context: This refactoring improves the domain model by properly representing following relationships as value objects. It enhances encapsulation and makes the domain model more expressive.

Ensure all existing tests continue to pass during and after the refactoring.
```

### Phase 3: Enhanced Social Interactions

#### Task 3.1: Mentions Feature

**Prompt:**
```
Implement the ability to mention users in messages using the "@username" syntax.

Specific Deliverables:
1. Enhance the Message domain model to:
   - Detect and extract mentions (using "@username" pattern)
   - Store references to mentioned users
   - Provide methods to query if a user is mentioned

2. Update TimelineService to:
   - Support finding mentions for a specific user
   - Filter messages where a user is mentioned

Language: Kotlin 2.1.21
Context: Users should be able to mention other users in their messages using the "@username" syntax. The application needs to detect these mentions and provide functionality to find all messages where a specific user is mentioned.

Write tests that validate mention detection and querying before implementing the feature.
```

#### Task 3.2: Links Feature

**Prompt:**
```
Implement URL detection and handling in messages.

Specific Deliverables:
1. Enhance the Message domain model to:
   - Detect URLs in message content
   - Extract URL information (links)
   - Provide methods to retrieve links from a message

2. Update TimelineService as needed to:
   - Support URL discovery and processing

Language: Kotlin 2.1.21
Context: Messages may contain URLs that should be detected and made clickable. The application needs to identify these URLs and provide functionality to extract them.

Write tests that validate URL detection and extraction before implementing the feature. Consider using regular expressions or pattern matching for URL detection.
```

#### Task 3.3: Direct Messages Feature

**Prompt:**
```
Implement direct messaging functionality between users.

Specific Deliverables:
1. Create DirectMessage domain model:
   - Content (text)
   - Sender (user)
   - Recipient (user)
   - Timestamp
   - Methods for message properties

2. Enhance TimelineService with:
   - Method to send a direct message to a specific user
   - Method to retrieve direct messages for a user
   - Proper ordering of direct messages (newest first)

Language: Kotlin 2.1.21
Context: Users should be able to send private messages to specific users. These messages should only be visible to the sender and the recipient.

Write tests that validate direct message sending and retrieval before implementing the feature. Consider how to ensure privacy constraints are maintained.
```

#### Task 3.4: Refactoring - Message Hierarchy

**Prompt:**
```
Refactor the message implementation to introduce a message hierarchy that handles different types of messages.

Specific Deliverables:
1. Create a proper message hierarchy:
   - BaseMessage abstract class or interface
   - TimelineMessage for public timeline posts
   - DirectMessage for private messages
   - Appropriate shared behavior and type-specific behavior

2. Refactor TimelineService:
   - Handle different message types appropriately
   - Maintain existing functionality with the new structure

Language: Kotlin 2.1.21
Context: This refactoring improves the domain model by creating a proper message hierarchy to distinguish between different types of messages. It enhances the model's expressiveness and maintainability.

Ensure all existing tests continue to pass during and after the refactoring. Consider using Kotlin's sealed classes if appropriate.
```

### Phase 4: Refactoring and Optimization

#### Task 4.1: Repository Pattern Implementation

**Prompt:**
```
Refactor the application to implement the Repository pattern for managing domain objects.

Specific Deliverables:
1. Create Repository interfaces and implementations:
   - TimelineRepository for managing Timeline aggregates
   - UserRepository for managing User entities
   - MessageRepository if needed

2. Refactor TimelineService:
   - Use repositories for data access
   - Focus on orchestrating domain operations
   - Delegate persistence concerns to repositories

Language: Kotlin 2.1.21
Context: This refactoring improves the architecture by separating domain logic from data access concerns. It makes the application more maintainable and testable.

Ensure all existing tests continue to pass during and after the refactoring. The repositories should still use in-memory storage as per the requirements.
```

#### Task 4.2: Domain Event Implementation

**Prompt:**
```
Implement Domain Events for important domain operations.

Specific Deliverables:
1. Create Domain Event classes:
   - MessagePosted event
   - UserFollowed event
   - UserMentioned event
   - DirectMessageSent event

2. Implement an event dispatcher mechanism:
   - A way to register event handlers
   - Methods to dispatch events
   - Basic event handling infrastructure

3. Update TimelineService:
   - Emit appropriate domain events
   - React to domain events as needed

Language: Kotlin 2.1.21
Context: Domain Events improve the architecture by decoupling different parts of the application and enabling reactivity to important domain operations. They allow for features like notifications to be implemented in a clean way.

Write tests that validate the event dispatching and handling before implementing the feature. Focus on making the event mechanism simple but functional.
```

#### Task 4.3: Performance Optimization

**Prompt:**
```
Optimize the performance of the application, particularly for timeline retrieval and aggregation.

Specific Deliverables:
1. Analyze and optimize timeline retrieval:
   - Efficient data structures for storing messages
   - Optimized algorithms for timeline aggregation
   - Consider caching strategies where appropriate

2. Implement performance improvements:
   - Optimize the implementation based on the analysis
   - Ensure correctness is maintained

Language: Kotlin 2.1.21
Context: As the application grows, performance becomes important, especially for operations like retrieving aggregated timelines of followed users. This task focuses on optimizing these operations without compromising correctness.

Write performance tests to measure before and after improvements. Ensure all functional tests continue to pass after optimization.
```

#### Task 4.4: Final Refactoring - Clean Architecture

**Prompt:**
```
Perform a final refactoring to align the application more closely with Clean Architecture principles.

Specific Deliverables:
1. Reorganize code into layers:
   - Domain layer (entities, value objects, domain services)
   - Application layer (use cases, application services)
   - Infrastructure layer (repositories, event dispatchers)

2. Enforce clear dependencies:
   - Ensure dependencies point inward
   - Use interfaces for crossing boundaries
   - Apply dependency injection where appropriate

Language: Kotlin 2.1.21
Context: This final refactoring improves the overall architecture of the application, making it more maintainable, testable, and aligned with Clean Architecture principles.

Ensure all existing tests continue to pass during and after the refactoring. Focus on maintaining the simplicity of the application while improving its structure.
```

## Additional Guidelines

1. **TDD Approach**: For each task, always write tests first that validate the required behavior. Only then implement the minimum code necessary to make the tests pass.

2. **TCR Workflow**: Use the TCR script (`./tcr.sh -c`) for each coding session. This will run tests, apply the commit or revert based on test results, and ensure a proper development workflow.

3. **Domain-Driven Design**: Apply DDD principles throughout the implementation:
   - Create rich domain models with behavior, not just data
   - Use Value Objects for concepts without identity
   - Implement Aggregates to enforce consistency boundaries
   - Apply Factory methods for complex object creation
   - Use Repositories for persistence
   - Implement Domain Events for communication

4. **Code Quality**: Follow Kotlin best practices and ensure code passes ktlint validation. Prioritize readability and maintainability.

5. **Refactoring**: After implementing each feature, consider refactoring to improve the design. Ensure all tests remain green during refactoring.

6. **In-Memory Approach**: Use in-memory data structures for storage. No database or external persistence is required.

7. **Incremental Development**: Build features incrementally, focusing on the simplest implementation that meets the requirements. Enhance and refactor as needed.