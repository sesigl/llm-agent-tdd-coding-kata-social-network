**Detailed Blueprint: Social Network Kata in Kotlin (TCR & LLM-Assisted)**

This document outlines an iterative plan to build the Social Network Kata using Kotlin, Maven, and a Test-Commit-Revert (TCR) workflow. Each step is designed to be a small, manageable chunk suitable for implementation, followed by a TCR cycle. Prompts are designed for an LLM to generate the necessary Kotlin code. The TCR cycle is invoked by running `./tcr.sh -c`.

---

## High-Level Features (Kata Requirements)

1.  **Posting**: Alice can publish messages to her personal timeline.
2.  **Reading**: Bob can view Alice’s timeline.
3.  **Following**: Charlie can subscribe to Alice’s and Bob’s timelines, and view an aggregated list of all subscriptions.
4.  **Mentions**: Bob can link to Charlie in a message using “@”.
5.  **Links**: Alice can link to a clickable web resource in a message.
6.  **Direct Messages**: Mallory can send a private message to Alice.

---

## Iterative Development Plan & LLM Prompts

**Initial Setup (Assumed complete based on user's provided project state):**
* Project initialized with Kotlin, Maven (`pom.xml`).
* `TimelineService.kt` and `TimelineServiceTest.kt` skeletons exist.
* `tcr.sh` script is in place and executable.
* JUnit 5 is configured.

---

### Phase 1: Posting and Reading Timelines

**Chunk 1.1: Alice Publishes a Message**

* **Goal**: Alice can post a message. The message is stored.
* **TCR Interaction**: After implementing the test and code, run `./tcr.sh -c`.

**Prompt 1.1.1 (Test - `TimelineServiceTest.kt`):**
```kotlin
// In `TimelineServiceTest.kt`, write a JUnit 5 test named `aliceCanPublishAMessageToHerTimeline`.
// This test should:
// 1. Create an instance of `TimelineService`.
// 2. Define a user "Alice" and a message "Hello, world!".
// 3. Call a method on `timelineService` (e.g., `publish`) for Alice to post the message.
// 4. Assert that when Alice's timeline is retrieved (e.g., via `getTimeline("Alice")`), it contains the published message.
//    - For now, a message can be a simple String.
//    - A timeline can be a `List<String>`.
//    - If `getTimeline` for a user who hasn't posted returns an empty list or null, handle that in the assertion (expect empty list).
// Assume `publish` and `getTimeline` methods will be created in `TimelineService`.
````

**Prompt 1.1.2 (Implementation - `TimelineService.kt`):**

```kotlin
// Based on the test `aliceCanPublishAMessageToHerTimeline`:
// In `TimelineService.kt`:
// 1. Create a private property to store timelines, perhaps a `MutableMap<String, MutableList<String>>` where the key is the username and the value is a list of messages. Initialize it as an empty map.
// 2. Implement the `publish(user: String, message: String)` method. It should add the message to the specified user's list of messages. If the user doesn't exist in the map, create a new timeline (list) for them first.
// 3. Implement the `getTimeline(user: String): List<String>` method. It should return the list of messages for the user. If the user has no messages or doesn't exist in the map, return an empty list.
```

*(After implementing Prompt 1.1.1 and 1.1.2, run `./tcr.sh -c`)*

-----

**Chunk 1.2: Timelines Show Multiple Messages in Order**

* **Goal**: Messages on a timeline appear in reverse chronological order (most recent first).
* **TCR Interaction**: After implementing the test and code, run `./tcr.sh -c`.

**Prompt 1.2.1 (Test - `TimelineServiceTest.kt`):**

```kotlin
// In `TimelineServiceTest.kt`, add a new JUnit 5 test named `aliceTimelineShowsMessagesInReverseChronologicalOrder`.
// This test should:
// 1. Create an instance of `TimelineService`.
// 2. Alice publishes "First message".
// 3. Alice then publishes "Second message".
// 4. When Alice's timeline is retrieved, assert that "Second message" appears before "First message" in the list.
```

**Prompt 1.2.2 (Implementation - `TimelineService.kt`):**

```kotlin
// Modify `TimelineService.kt` to ensure messages are stored and retrieved in reverse chronological order.
// - When `publish`, add new messages to the beginning of the user's list (index 0).
// - The `getTimeline` method should then naturally return them in this order without needing to reverse a copy.
```

*(After implementing Prompt 1.2.1 and 1.2.2, run `./tcr.sh -c`)*

-----

**Chunk 1.3: Bob Views Alice's Timeline**

* **Goal**: One user can view another user's timeline.
* **TCR Interaction**: After implementing the test and code, run `./tcr.sh -c`.

**Prompt 1.3.1 (Test - `TimelineServiceTest.kt`):**

```kotlin
// In `TimelineServiceTest.kt`, add a JUnit 5 test named `bobCanViewAlicesTimeline`.
// This test should:
// 1. Create `TimelineService`.
// 2. Alice publishes "Alice's message".
// 3. Bob retrieves Alice's timeline using `getTimeline("Alice")`.
// 4. Assert that Bob sees "Alice's message" on Alice's timeline.
// This test primarily verifies that `getTimeline` works for users other than the poster.
```

**Prompt 1.3.2 (Implementation - `TimelineService.kt`):**

```kotlin
// Review `TimelineService.kt`. The existing `getTimeline(user: String)` method should already support this functionality if it correctly retrieves any user's timeline by their name and was not written with owner-only assumptions. No changes are likely needed.
```

*(After implementing Prompt 1.3.1 and 1.3.2, run `./tcr.sh -c`)*

-----

### Phase 2: Following

**Chunk 2.1: Charlie Subscribes to Alice's Timeline**

* **Goal**: Charlie can subscribe to Alice.
* **TCR Interaction**: After implementing the test and code, run `./tcr.sh -c`.

**Prompt 2.1.1 (Test - `TimelineServiceTest.kt`):**

```kotlin
// In `TimelineServiceTest.kt`, add a new JUnit 5 test named `charlieCanSubscribeToAlicesTimeline`.
// This test should:
// 1. Create `TimelineService`.
// 2. Charlie calls a method on `timelineService` to follow Alice (e.g., `follow("Charlie", "Alice")`).
// 3. Assert that Charlie is now following Alice. This can be verified by adding a method like `getSubscriptions(user: String): Set<String>` to `TimelineService` and checking its output.
//    Assert that `getSubscriptions("Charlie")` returns a set containing "Alice".
```

**Prompt 2.1.2 (Implementation - `TimelineService.kt`):**

```kotlin
// In `TimelineService.kt`:
// 1. Add a private data structure to store subscriptions, e.g., `subscriptions: MutableMap<String, MutableSet<String>> = mutableMapOf()`, where key is the follower and value is a set of users they follow.
// 2. Implement `follow(follower: String, followee: String)`. It should record that `follower` now follows `followee`. Ensure `followee` is added to the `follower`'s set in the map. Initialize the set if the follower is not already in the map.
// 3. Implement `getSubscriptions(user: String): Set<String>`. It should return the set of users `user` is following. If the user is not following anyone or not in the map, return an empty set.
```

*(After implementing Prompt 2.1.1 and 2.1.2, run `./tcr.sh -c`)*

-----

**Chunk 2.2: Introduce Message Data Class and Timestamps**

* **Goal**: Refactor to use a `Message` data class with timestamps for proper ordering in aggregated walls.
* **TCR Interaction**: After implementing, run `./tcr.sh -c`.

**Prompt 2.2.1 (Data Class and Refactor - `Message.kt`, `TimelineService.kt`, `TimelineServiceTest.kt`):**

```kotlin
// 1. Create a new Kotlin file `Message.kt` in the `org.codingkata.socialnetwork` package.
//    Define a data class:
//    `data class Message(val user: String, val content: String, val timestamp: Long = System.currentTimeMillis())`

// 2. In `TimelineService.kt`:
//    - Change the timeline storage from `MutableMap<String, MutableList<String>>` to `timelines: MutableMap<String, MutableList<Message>> = mutableMapOf()`.
//    - Update `publish(user: String, messageContent: String)`:
//      - It should now create a `Message` object (e.g., `Message(user, messageContent, System.currentTimeMillis())`).
//      - Add this `Message` object to the user's timeline at the beginning of the list.
//    - Update `getTimeline(user: String): List<Message>`. It should return `List<Message>`.

// 3. In `TimelineServiceTest.kt`:
//    - Update existing tests (`aliceCanPublishAMessageToHerTimeline`, `aliceTimelineShowsMessagesInReverseChronologicalOrder`, `bobCanViewAlicesTimeline`) to work with `List<Message>`.
//    - For assertions, you might check `message.content` or the whole `Message` object.
//    - Example for `aliceCanPublishAMessageToHerTimeline`:
//      `val timeline = timelineService.getTimeline("Alice")`
//      `assertTrue(timeline.any { it.user == "Alice" && it.content == "Hello, world!" })`
//    - For `aliceTimelineShowsMessagesInReverseChronologicalOrder`, ensure you are comparing `Message` objects or their content in the correct order. You might need to use `Thread.sleep(1)` between publishes if `System.currentTimeMillis()` is too fast, or pass explicit timestamps for testing.
//      `val messages = timelineService.getTimeline("Alice")`
//      `assertEquals("Second message", messages[0].content)`
//      `assertEquals("First message", messages[1].content)`
```

*(After implementing Prompt 2.2.1, run `./tcr.sh -c`)*

-----

**Chunk 2.3: Charlie Views Aggregated Wall**

* **Goal**: Charlie sees an aggregated list of messages from users he follows, in reverse chronological order.
* **TCR Interaction**: After implementing, run `./tcr.sh -c`.

**Prompt 2.3.1 (Test - `TimelineServiceTest.kt`):**

```kotlin
// In `TimelineServiceTest.kt`, add a JUnit 5 test named `charlieSeesAggregatedWallInReverseChronologicalOrder`.
// This test should:
// 1. Create `TimelineService`.
// 2. Alice publishes "Alice's first post". Use `Thread.sleep(5)` between posts to ensure distinct timestamps for reliable testing.
// 3. Bob publishes "Bob's first post". `Thread.sleep(5)`.
// 4. Charlie follows Alice.
// 5. Charlie follows Bob.
// 6. Alice publishes "Alice's second post". `Thread.sleep(5)`.
// 7. Bob publishes "Bob's second post".
// 8. David (not followed by Charlie) publishes "David's post".
// 9. When Charlie gets his wall (e.g., `getWall("Charlie"): List<Message>`), assert that the messages are ordered by timestamp descending:
//    - Bob's second post
//    - Alice's second post
//    - Bob's first post
//    - Alice's first post
//    Assert that "David's post" is NOT on Charlie's wall.
//    The wall should contain messages only from Alice and Bob, and their content should match.
```

**Prompt 2.3.2 (Implementation - `TimelineService.kt`):**

```kotlin
// In `TimelineService.kt`:
// Implement `getWall(user: String): List<Message>`:
//  - Get the set of users `user` is following using `getSubscriptions(user)`.
//  - If the user follows no one, return an empty list.
//  - Collect all messages from the timelines of all followed users.
//    - For each `followedUser` in the set, get their timeline using `getTimeline(followedUser)`.
//    - Add all messages from these timelines to a temporary list.
//  - Sort the combined list of messages in descending order by `timestamp`.
//  - Return the sorted list.
```

*(After implementing Prompt 2.3.1 and 2.3.2, run `./tcr.sh -c`)*

-----

### Phase 3: Mentions

**Chunk 3.1: Bob Mentions Charlie**

* **Goal**: Messages can contain "@username" mentions. The service stores them as part of the message content.
* **TCR Interaction**: After implementing, run `./tcr.sh -c`.

**Prompt 3.1.1 (Test - `TimelineServiceTest.kt`):**

```kotlin
// In `TimelineServiceTest.kt`, add a JUnit 5 test named `messageCanContainMentions`.
// This test should:
// 1. Create `TimelineService`.
// 2. Bob publishes a message: "Hello @Charlie, how are you?".
// 3. Retrieve Bob's timeline.
// 4. Assert that one of the messages on Bob's timeline has the content "Hello @Charlie, how are you?".
```

**Prompt 3.1.2 (Implementation - `TimelineService.kt`):**

```kotlin
// Review `TimelineService.kt` and `Message.kt`.
// No specific changes should be needed if `Message.content` already stores the raw string. This test confirms that mentions are not altered or stripped.
```

*(After implementing Prompt 3.1.1 and 3.1.2, run `./tcr.sh -c`)*

-----

### Phase 4: Links

**Chunk 4.1: Alice Posts a Link**

* **Goal**: Messages can contain web links. The service stores them as part of the message content.
* **TCR Interaction**: After implementing, run `./tcr.sh -c`.

**Prompt 4.1.1 (Test - `TimelineServiceTest.kt`):**

```kotlin
// In `TimelineServiceTest.kt`, add a JUnit 5 test named `messageCanContainLinks`.
// This test should:
// 1. Create `TimelineService`.
// 2. Alice publishes a message: "Check out this site: [http://example.com](http://example.com)".
// 3. Retrieve Alice's timeline.
// 4. Assert that one of the messages on Alice's timeline has the content "Check out this site: [http://example.com](http://example.com)".
```

**Prompt 4.1.2 (Implementation - `TimelineService.kt`):**

```kotlin
// Review `TimelineService.kt` and `Message.kt`.
// No specific changes should be needed if `Message.content` already stores the raw string. This test confirms that links are not altered or stripped.
```

*(After implementing Prompt 4.1.1 and 4.1.2, run `./tcr.sh -c`)*

-----

### Phase 5: Direct Messages

**Chunk 5.1: Refactor Message for DMs and Implement DM Sending/Receiving**

* **Goal**: Mallory sends a DM to Alice. Only Alice (and Mallory in her "sent" view) can see it.
* **TCR Interaction**: After each test/impl pair, run `./tcr.sh -c`.

**Prompt 5.1.1 (Refactor `Message.kt`):**

```kotlin
// In `Message.kt`, update the `Message` data class to support direct messages:
// `data class Message(`
// `    val user: String, // Sender of the message`
// `    val content: String,`
// `    val timestamp: Long = System.currentTimeMillis(),`
// `    val recipient: String? = null // null for public posts, username for DMs`
// `)`
// Also, update any existing tests in `TimelineServiceTest.kt` if this change requires constructor adjustments for public messages (they will now have `recipient = null` by default if not specified, or you can pass it explicitly).
```

*(After implementing Prompt 5.1.1 and ensuring existing tests pass, run `./tcr.sh -c`)*

**Prompt 5.1.2 (Test - `TimelineServiceTest.kt` - Mallory Sends DM):**

```kotlin
// In `TimelineServiceTest.kt`, add a JUnit 5 test named `malloryCanSendDirectMessageToAliceAndAliceReceivesIt`.
// This test should:
// 1. Create `TimelineService`.
// 2. Mallory sends a direct message "DM to Alice" to Alice using a new method, e.g., `sendDirectMessage("Mallory", "Alice", "DM to Alice")`.
// 3. Alice retrieves her direct messages (e.g., using a new method `getDirectMessages("Alice"): List<Message>`).
// 4. Assert that Alice's direct messages list contains one message.
// 5. Assert that this message has `user`="Mallory", `recipient`="Alice", and `content`="DM to Alice".
```

**Prompt 5.1.3 (Implementation - `TimelineService.kt` - Send and Get DMs):**

```kotlin
// In `TimelineService.kt`:
// 1. Implement `sendDirectMessage(sender: String, recipientUser: String, messageContent: String)`:
//    - Create a `Message` object with `user = sender`, `content = messageContent`, `recipient = recipientUser`.
//    - Store this message. DMs are still associated with the sender's timeline. Add it to `timelines[sender]`.
//      (This means `timelines` now holds both public posts from a user and DMs sent by that user).

// 2. Implement `getDirectMessages(user: String): List<Message>`:
//    - This method needs to find all messages where `user` is the recipient OR `user` is the sender and it's a DM.
//    - Initialize an empty `mutableListOf<Message>()`.
//    - Iterate through all users (`poster`) in `timelines.keys`.
//    - For each `posterMessages` in `timelines[poster]`:
//        - Iterate through each `message` in `posterMessages`.
//        - If `message.recipient == user` (message sent to `user`), add it to the list.
//        - Else if `message.user == user && message.recipient != null` (DM sent by `user`), add it to the list.
//    - Sort the collected DMs by `timestamp` in descending order.
//    - Return the sorted list.
```

*(After implementing Prompt 5.1.2 and 5.1.3, run `./tcr.sh -c`)*

**Prompt 5.1.4 (Test - `TimelineServiceTest.kt` - DM Not on Public Timeline):**

```kotlin
// In `TimelineServiceTest.kt`, add a test: `directMessageToAliceIsNotOnHerPublicTimeline`.
// 1. Create `TimelineService`.
// 2. Alice posts a public message "Alice's public post". `Thread.sleep(5)`.
// 3. Mallory sends a direct message "DM to Alice" to Alice.
// 4. Modify `TimelineService.getTimeline(user: String)` to filter out DMs.
//    When anyone (e.g. Bob) views Alice's public timeline (`getTimeline("Alice")`), assert:
//    - It contains a message with content "Alice's public post".
//    - It *does not* contain any message with content "DM to Alice".
//    - It should only contain messages where `recipient` is `null`.
```

**Prompt 5.1.5 (Implementation - `TimelineService.kt` - Filter Public Timeline):**

```kotlin
// In `TimelineService.kt`:
// Modify `getTimeline(user: String): List<Message>`:
//  - Fetch messages from `timelines.getOrDefault(user, emptyList())`.
//  - Filter this list to include only messages where `message.recipient == null` (i.e., public posts by that user).
//  - Ensure it still returns messages in reverse chronological order (which it should if they are added to the front).
```

*(After implementing Prompt 5.1.4 and 5.1.5, run `./tcr.sh -c`)*

**Prompt 5.1.6 (Test - `TimelineServiceTest.kt` - DM Not on Follower's Wall):**

```kotlin
// In `TimelineServiceTest.kt`, add one more test: `directMessageToAliceIsNotOnFollowersWall`.
// 1. Create `TimelineService`.
// 2. Charlie follows Alice.
// 3. Alice posts a public message "Alice's public post for followers". `Thread.sleep(5)`.
// 4. Mallory sends a direct message "Secret DM to Alice" to Alice.
// 5. When Charlie views his wall (`getWall("Charlie")`), assert:
//    - It contains a message with content "Alice's public post for followers".
//    - It *does not* contain any message with content "Secret DM to Alice".
//    - The wall should only consist of public messages from followed users.
```

**Prompt 5.1.7 (Implementation - `TimelineService.kt` - Verify Wall):**

```kotlin
// Review `TimelineService.kt`.
// The `getWall(user: String)` method should already correctly exclude DMs if `getTimeline(followedUser)` (which `getWall` uses internally) is correctly filtering to only public posts.
// No specific changes to `getWall` should be needed if `getTimeline` is correct. This test confirms the integration.
```

*(After implementing Prompt 5.1.6 and 5.1.7, run `./tcr.sh -c`)*

-----

### Refactoring Steps

To ensure the code remains clean and maintainable, the following refactoring steps should be applied after each phase or feature implementation:

1. **Consolidate Repeated Logic**:
    - Identify and extract any repeated logic into helper methods or utility classes.
    - For example, if multiple methods initialize or manipulate timelines, centralize this logic.

2. **Improve Data Structures**:
    - Replace simple data structures with more robust ones as requirements evolve.
    - For instance, refactor `MutableMap<String, MutableList<String>>` to use a `Message` data class for better extensibility.

3. **Enhance Readability**:
    - Rename variables and methods to better reflect their purpose.
    - Add comments or documentation for complex logic.

4. **Optimize Performance**:
    - Review and optimize any inefficient operations, such as sorting or filtering large datasets.

5. **Modularize Code**:
    - Break down large classes or methods into smaller, more focused components.
    - For example, extract timeline-related logic into a separate `TimelineManager` class.

6. **Test Coverage**:
    - Ensure all edge cases are covered by tests.
    - Add tests for scenarios that were not initially considered.

7. **Code Formatting**:
    - Apply consistent code formatting and adhere to Kotlin coding conventions.

By incorporating these refactoring steps, the codebase will remain clean, maintainable, and scalable as new features are added.

## Conclusion

This blueprint provides an iterative path through the Social Network Kata requirements using Kotlin. Each prompt is designed to build a small piece of functionality. After implementing the code for a prompt (or a test/implementation pair), run `./tcr.sh -c` to commit valid changes or revert if tests fail.

Remember to use `Thread.sleep(some_small_milliseconds)` between message publications in tests if `System.currentTimeMillis()` does not provide enough resolution for distinct timestamps, or consider passing explicit timestamps to `Message` objects during tests for more deterministic behavior.