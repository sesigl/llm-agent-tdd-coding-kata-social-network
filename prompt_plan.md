# Social Network Coding Kata Implementation Blueprint

This document outlines a detailed plan for implementing a social network application in Kotlin, following TDD+TCR principles and Domain-Driven Design practices. The implementation will progressively build features while maintaining high code quality through regular refactoring.

## High-Level Phases

1. **Core Domain Model & Timeline Publishing**
   - Define essential domain entities and value objects
   - Implement posting messages to personal timelines

2. **Timeline Reading & Query Capabilities**
   - Enable reading timeline messages
   - Implement chronological ordering

3. **Social Graph & Aggregated Views**
   - Implement following/subscription mechanism
   - Create aggregated timeline views

4. **Enhanced Content Features**
   - Implement @mentions
   - Support clickable web links

5. **Privacy & Permissions**
   - Implement direct messaging
   - Add visibility controls

---

## Phase 1: Core Domain Model & Timeline Publishing

### Task 1.1: Define Core Domain Model ☐

**Goal:** Create the essential domain entities and value objects needed for the social network

**Deliverables:**
- Value objects:
  - `UserId.kt` - Represents a unique user identifier
  - `Message.kt` - Represents a timeline message with content and metadata
  - `Timestamp.kt` - Wraps DateTime functionality for domain clarity
- Repository interface:
  - `TimelineRepository.kt` - Repository for storing and retrieving timeline messages

**Implementation Prompt:**
```
Implement the core domain model for a social network in Kotlin, using TDD and Domain-Driven Design principles.

Start by creating these value objects:
1. UserId - A wrapper around String that represents a unique user identifier
2. Message - A data class containing message content, author (UserId), and timestamp
3. Timestamp - A wrapper around java.time.Instant for domain clarity

Then create a repository interface:
- TimelineRepository - Interface with methods to store and retrieve messages

For each class:
- First write tests in the corresponding test file
- Then implement the minimal required functionality
- Use immutable properties (val) and proper encapsulation
- Implement proper equals/hashCode for value objects
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 1.2: Implement Timeline Service Publishing ☐

**Goal:** Enable users to publish messages to their personal timelines

**Deliverables:**
- Service implementation:
  - Modify `TimelineService.kt` to handle message publishing
- Repository implementation:
  - `InMemoryTimelineRepository.kt` - In-memory implementation of TimelineRepository
- Unit tests for both components

**Implementation Prompt:**
```
Implement the ability for users to publish messages to their personal timelines in Kotlin.

1. Update TimelineService.kt to include:
   - A dependency on TimelineRepository
   - A postMessage(userId: UserId, content: String) method that creates and stores a new message

2. Create InMemoryTimelineRepository.kt:
   - Implement the TimelineRepository interface
   - Use a concurrent map to store messages by userId
   - Store messages in chronological order

For each component:
- First write tests in the corresponding test file (TimelineServiceTest.kt)
- Test edge cases like empty messages or null values
- Then implement the minimal required functionality
- Use immutable data structures where possible
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 1.3: Refactoring - Domain Model Enrichment ☐

**Goal:** Refactor the initial domain model to add richer semantics and validation

**Deliverables:**
- Enhanced domain objects with validation and factories:
  - Update `Message.kt` to validate content
  - Add factory methods for more controlled object creation

**Implementation Prompt:**
```
Refactor the initial domain model to enhance validation and semantic clarity using DDD principles.

1. Enhance the Message value object:
   - Add content validation (non-empty, max length)
   - Create a companion object with factory methods for message creation
   - Consider using Result<Message> for failure handling instead of exceptions
   - Make the constructor private and expose factory methods

2. Update any services using Message to leverage the new factory methods

For each refinement:
- First modify or extend tests to verify new validation logic
- Then implement the enhancements 
- Ensure all tests continue to pass
- Make sure the code meets ktlint standards

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

---

## Phase 2: Timeline Reading & Query Capabilities

### Task 2.1: Implement Timeline Reading ☐

**Goal:** Enable users to view another user's timeline

**Deliverables:**
- Enhanced TimelineService:
  - Add methods to retrieve a user's timeline
  - Return messages in chronological order (newest first)
- Tests covering timeline retrieval

**Implementation Prompt:**
```
Implement timeline reading capabilities in Kotlin, allowing users to view other users' timelines.

1. Update TimelineService.kt to include:
   - A getTimeline(userId: UserId) method that returns a user's messages in chronological order
   - Proper handling for users with no messages (return empty list, not null)

2. Update InMemoryTimelineRepository.kt if needed:
   - Ensure retrieval of messages is efficient
   - Maintain chronological ordering

3. For testing:
   - Create scenarios with multiple users
   - Test timeline retrieval for various users
   - Verify chronological ordering (newest first)
   - Test edge cases (non-existent users, empty timelines)

For each component:
- First write tests in TimelineServiceTest.kt
- Then implement the minimal required functionality
- Use immutable data structures and functional approaches (map, filter) where appropriate
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 2.2: Add Richer Timeline Querying ☐

**Goal:** Enhance timeline reading with filtering and pagination

**Deliverables:**
- New value object:
  - `TimelineQuery.kt` - Encapsulates query parameters (limit, before/after timestamps)
- Enhanced service methods:
  - Add overloaded `getTimeline` method that accepts TimelineQuery

**Implementation Prompt:**
```
Enhance timeline reading capabilities by adding filtering and pagination in Kotlin.

1. Create a TimelineQuery value object:
   - Include optional parameters: limit, beforeTimestamp, afterTimestamp
   - Provide factory methods for common query patterns
   - Implement validation for query parameters

2. Update TimelineService.kt:
   - Add a getTimeline(userId: UserId, query: TimelineQuery) method
   - Implement filtering based on query parameters
   - Respect the limit parameter for pagination

3. Update the repository as needed to support efficient querying

For testing:
- Create complex timeline scenarios with many messages
- Test pagination (limiting results)
- Test time-based filtering (before/after specific timestamps)
- Test combinations of query parameters
- Verify ordering remains correct

For each component:
- First write tests in corresponding test files
- Then implement the minimal required functionality
- Use functional programming approaches for filtering and mapping
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 2.3: Refactoring - Introduce Timeline Aggregate ☐

**Goal:** Refactor to use proper DDD Aggregate pattern for timelines

**Deliverables:**
- New domain objects:
  - `Timeline.kt` - Aggregate root representing a user's timeline
  - `TimelineId.kt` - Identity for Timeline aggregate
- Updated repository:
  - Refactor to work with Timeline aggregates

**Implementation Prompt:**
```
Refactor the current implementation to use proper DDD Aggregate pattern for timelines in Kotlin.

1. Create a Timeline aggregate root:
   - Create TimelineId (wrapping UserId)
   - Implement Timeline class with encapsulated collection of messages
   - Include methods for adding messages and querying (encapsulate business rules)
   - Ensure invariants are maintained (e.g., messages are always ordered)

2. Update TimelineRepository to work with Timeline aggregates:
   - Methods should accept/return Timeline objects instead of raw messages
   - Preserve existing functionality but through the aggregate

3. Update TimelineService to use the new aggregate:
   - Adapt existing methods to work with Timeline objects
   - Maintain the same public API for compatibility

For this refactoring:
- Update tests to verify the same behavior through the new structure
- Use test-driven approach even for refactoring
- Ensure all functionality remains working
- Focus on proper encapsulation and domain invariants
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

---

## Phase 3: Social Graph & Aggregated Views

### Task 3.1: Implement Following Mechanism ☐

**Goal:** Enable users to follow other users

**Deliverables:**
- New domain objects:
  - `Follow.kt` - Represents a follow relationship between users
  - `SocialGraph.kt` - Domain service managing follow relationships
- New repository:
  - `FollowRepository.kt` - Repository interface for follow relationships
  - `InMemoryFollowRepository.kt` - Implementation of FollowRepository

**Implementation Prompt:**
```
Implement a following mechanism in Kotlin, enabling users to follow other users.

1. Create new domain objects:
   - Follow - A value object representing a follow relationship (follower + followee)
   - SocialGraph - A domain service to manage follow relationships

2. Implement repositories:
   - FollowRepository interface with methods to create/remove follows and query relationships
   - InMemoryFollowRepository implementation using concurrent collections

3. Update TimelineService:
   - Add followUser(followerId: UserId, followeeId: UserId) method
   - Add unfollowUser(followerId: UserId, followeeId: UserId) method
   - Add getFollowing(userId: UserId) method returning users followed by userId

For testing:
- Test following and unfollowing users
- Test querying a user's follow list
- Test edge cases (following self, following same user twice)
- Test error conditions (non-existent users)

For each component:
- First write tests in corresponding test files
- Then implement the minimal required functionality
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 3.2: Implement Aggregated Timeline ☐

**Goal:** Allow users to view an aggregated timeline of all followed users' posts

**Deliverables:**
- Enhanced TimelineService:
  - Add method to retrieve aggregated timeline for a user
- Tests covering aggregated timeline retrieval

**Implementation Prompt:**
```
Implement an aggregated timeline view in Kotlin, showing posts from all followed users.

1. Update TimelineService.kt:
   - Add getAggregatedTimeline(userId: UserId) method that returns a chronologically ordered list of messages from all users followed by userId
   - Add getAggregatedTimeline(userId: UserId, query: TimelineQuery) overload for pagination and filtering

2. For the aggregation logic:
   - Get all users followed by userId
   - Fetch messages from each followed user's timeline
   - Merge and sort all messages by timestamp (newest first)
   - Apply any query filters (limit, time range)

For testing:
- Create complex scenarios with multiple users following each other
- Populate timelines with various messages at different times
- Verify aggregated timeline contains all expected messages in correct order
- Test with query parameters (pagination, time filtering)
- Test edge cases (user with no follows, all followed users have empty timelines)

For each component:
- First write tests in TimelineServiceTest.kt
- Then implement the minimal required functionality
- Use functional programming approaches for aggregation
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 3.3: Refactoring - Domain Events ☐

**Goal:** Refactor to use Domain Events for cross-aggregate communication

**Deliverables:**
- New components:
  - `DomainEvent.kt` - Interface for domain events
  - `TimelineEvents.kt` - Timeline-related events
  - `FollowEvents.kt` - Follow-related events
  - `EventBus.kt` - Simple event bus for publishing/subscribing to events

**Implementation Prompt:**
```
Refactor the application to use Domain Events for communication between aggregates in Kotlin.

1. Create event infrastructure:
   - DomainEvent interface with common properties (timestamp, id)
   - SimpleEventBus implementation for publishing/subscribing to events
   - Specific event classes: MessagePosted, UserFollowed, UserUnfollowed

2. Update Timeline and SocialGraph to publish events:
   - When a message is posted, publish MessagePosted event
   - When a follow is created/removed, publish UserFollowed/UserUnfollowed events

3. Create subscribers for relevant events:
   - Update appropriate services to subscribe to events they're interested in

For testing:
- Test event publishing when actions occur
- Test event subscription and handling
- Ensure existing functionality remains intact
- Verify that cross-aggregate communication works correctly

For each component:
- First write tests verifying event publication and subscription
- Then implement the minimal required functionality
- Use proper encapsulation and immutable event objects
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

---

## Phase 4: Enhanced Content Features

### Task 4.1: Implement @ Mentions ☐

**Goal:** Support mentioning users with @ syntax in messages

**Deliverables:**
- Enhanced domain objects:
  - Update `Message.kt` to support mentions
  - Add `Mention.kt` value object
- New service methods:
  - Add mention detection and extraction functionality

**Implementation Prompt:**
```
Implement @mentions functionality in Kotlin, allowing users to mention others in messages.

1. Create Mention value object:
   - Should contain userId being mentioned
   - Track position in the message for potential highlighting

2. Update Message domain object:
   - Add a mentions property containing detected mentions
   - Update factory methods to parse message content for @mentions
   - Consider implementing content validation for correct @mention format

3. Update TimelineService:
   - Add getMentions(userId: UserId) method that returns messages where the user is mentioned
   - Ensure mentions are preserved when retrieving messages

For mention detection:
- Detect @username pattern in message content
- Map usernames to valid UserIds
- Store both the mention text and the resolved UserId

For testing:
- Test message creation with various mention patterns
- Test mention extraction and querying
- Test edge cases (invalid mentions, multiple mentions)
- Test retrieving mentions for a specific user

For each component:
- First write tests in corresponding test files
- Then implement the minimal required functionality
- Use regex or string parsing appropriately for mention detection
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 4.2: Implement Clickable Web Links ☐

**Goal:** Enable detection and interaction with URLs in messages

**Deliverables:**
- Enhanced domain objects:
  - Update `Message.kt` to support links
  - Add `Link.kt` value object
- New service methods:
  - Add link detection and extraction functionality

**Implementation Prompt:**
```
Implement clickable web links functionality in Kotlin, allowing URLs in messages to be interactive.

1. Create Link value object:
   - Should contain original URL text
   - Include normalized/validated URL
   - Track position in the message for potential highlighting

2. Update Message domain object:
   - Add a links property containing detected links
   - Update factory methods to parse message content for URLs
   - Add validation for URLs (valid format, etc.)

3. Update TimelineService as needed:
   - Ensure links are preserved when retrieving messages

For link detection:
- Detect URL patterns in message content (http://, https://, www.)
- Validate detected URLs (basic format validation)
- Normalize URLs for consistency

For testing:
- Test message creation with various URL patterns
- Test link extraction and storage
- Test edge cases (invalid URLs, multiple URLs, URLs with special characters)
- Test link rendering/retrieval in timelines

For each component:
- First write tests in corresponding test files
- Then implement the minimal required functionality
- Consider using standard URL validation libraries if needed
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 4.3: Refactoring - Rich Content Processing ☐

**Goal:** Refactor to use a more extensible content processing pipeline

**Deliverables:**
- New components:
  - `ContentProcessor.kt` - Interface for content processors
  - `MentionProcessor.kt` - Implementation for mention processing
  - `LinkProcessor.kt` - Implementation for link processing
- Updated domain objects to use the processing pipeline

**Implementation Prompt:**
```
Refactor the content processing logic to use a more extensible pipeline approach in Kotlin.

1. Create a ContentProcessor interface:
   - Define process(content: String) method returning processed content with metadata
   - Allow for chain-of-responsibility pattern

2. Implement specific processors:
   - MentionProcessor for @mentions
   - LinkProcessor for URLs
   - Consider a CompositeProcessor that combines multiple processors

3. Update Message domain:
   - Use processors in factory methods
   - Store processed content and metadata

4. Ensure API compatibility:
   - Maintain existing service methods and behavior
   - Enhance with new capabilities where appropriate

For testing:
- Update tests to verify the same behavior through the new structure
- Add tests for the processing pipeline specifically
- Test combining multiple processors
- Ensure all functionality remains working

For each component:
- First write tests in corresponding test files
- Then implement the refactoring
- Focus on extensibility and separation of concerns
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

---

## Phase 5: Privacy & Permissions

### Task 5.1: Implement Direct Messaging ☐

**Goal:** Enable users to send private messages visible only to the recipient

**Deliverables:**
- New domain objects:
  - `DirectMessage.kt` - Private message between users
  - `Visibility.kt` - Enum or value object for message visibility
- Enhanced services:
  - Add methods for sending/receiving direct messages

**Implementation Prompt:**
```
Implement direct messaging functionality in Kotlin, allowing users to send private messages.

1. Create new domain objects:
   - DirectMessage - Extended from or related to Message, with recipient information
   - Visibility - Enum or value object defining visibility levels (Public, Private)

2. Update or extend repositories:
   - Add support for storing and retrieving direct messages
   - Ensure privacy constraints are enforced at the repository level

3. Update TimelineService:
   - Add sendDirectMessage(senderId: UserId, recipientId: UserId, content: String) method
   - Add getDirectMessages(userId: UserId) method returning direct messages for a user
   - Ensure private messages only appear for sender and recipient

For testing:
- Test sending direct messages between users
- Test retrieving direct messages
- Test privacy constraints (messages only visible to sender and recipient)
- Test integration with existing timeline features

For each component:
- First write tests in corresponding test files
- Then implement the minimal required functionality
- Focus on proper encapsulation of privacy rules
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 5.2: Implement Visibility Controls ☐

**Goal:** Add granular visibility controls for all messages

**Deliverables:**
- Enhanced domain objects:
  - Update `Message.kt` with visibility settings
- New components:
  - `PermissionService.kt` - Service for checking message access permissions

**Implementation Prompt:**
```
Implement granular visibility controls for messages in Kotlin, extending beyond simple public/private.

1. Enhance Visibility value object or enum:
   - Add more visibility levels (Public, FollowersOnly, SpecificUsers, Private)
   - Include properties for specific allowed users where relevant

2. Update Message domain:
   - Add visibility property to control who can see the message
   - Update factory methods to include visibility options
   - Ensure immutability while supporting these new properties

3. Create PermissionService:
   - Implement canViewMessage(userId: UserId, message: Message) method
   - Enforce visibility rules based on social graph and message settings

4. Update TimelineService:
   - Filter timeline results based on visibility permissions
   - Add methods to post messages with specific visibility
   - Ensure compatibility with existing timeline features

For testing:
- Test creating messages with different visibility settings
- Test permission checks for various scenarios
- Test timeline filtering based on permissions
- Test integration with follows, mentions, and direct messages

For each component:
- First write tests in corresponding test files
- Then implement the minimal required functionality
- Focus on maintaining encapsulation of visibility rules
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

### Task 5.3: Final Refactoring - Domain Model Consolidation ☐

**Goal:** Consolidate the domain model for clarity, consistency, and maintainability

**Deliverables:**
- Refactored domain model with:
  - Clear aggregate boundaries
  - Consistent naming
  - Comprehensive documentation
  - Full test coverage

**Implementation Prompt:**
```
Perform a final refactoring to consolidate the domain model in Kotlin, focusing on clarity and consistency.

1. Review and refine aggregate boundaries:
   - Ensure Timeline, User/SocialGraph, and Message aggregates have clear responsibilities
   - Review entity vs. value object decisions for correctness
   - Verify domain invariants are properly enforced

2. Standardize naming and patterns:
   - Ensure consistent naming conventions across domain objects
   - Verify factory methods follow a consistent pattern
   - Review error handling and validation approaches for consistency

3. Add comprehensive documentation:
   - Add KDoc comments to all public methods and classes
   - Document domain concepts and design decisions
   - Create a domain model diagram if possible

4. Enhance test coverage:
   - Review and add tests for any uncovered edge cases
   - Ensure all business rules are explicitly tested
   - Verify integration between components works correctly

For this refactoring:
- Take an iterative approach, making small changes
- Focus on one aspect at a time (e.g., one aggregate)
- Use test-driven approach even for refactoring
- Ensure backwards compatibility with existing tests
- Follow Kotlin best practices and ktlint rules

Execute a TCR cycle after each test + implementation using ./tcr.sh -c
```

---

## Working with This Plan

This implementation plan is designed to be followed sequentially, with each task building on previous ones. For each task:

1. Read the implementation prompt carefully to understand the specific goal and deliverables
2. Follow TDD principles:
   - Write failing tests first
   - Implement minimal code to make tests pass
   - Refactor while keeping tests green
3. Execute TCR cycles using `./tcr.sh -c` after each test+implementation pair
4. Mark tasks as completed (☑️) once finished and verified

The plan integrates Domain-Driven Design principles progressively, starting with simple implementations and refactoring toward richer domain models as the application evolves.