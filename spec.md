**Project Plan: Social Network Kata in Kotlin (TCR Workflow)**

---

### Overview

We are building a simplified social networking service using Kotlin. The development will strictly follow the Test-Commit-Revert (TCR) methodology. The core functionalities will be implemented incrementally as per the kata requirements.

The application will support:

1.  **Posting**: Users can publish messages to their personal timeline.
2.  **Reading**: Users can view another user's timeline.
3.  **Following**: Users can subscribe to other users' timelines and view an aggregated feed of messages from all followed timelines.
4.  **Mentions**: Users can mention other users in their messages (e.g., using "@username").
5.  **Links**: Users can include clickable web links in their messages.
6.  **Direct Messages**: Users can send private messages to other users, visible only to the sender and recipient.

---

### Key Technical Aspects

1.  **Language**: Kotlin
2.  **Build Tool**: Maven (as per existing `pom.xml`)
3.  **Testing Framework**: JUnit 5 (as per `pom.xml` dependencies)
4.  **Development Methodology**: Test-Commit-Revert (TCR) using the provided `tcr.sh` script.
    * Execute `./tcr.sh -c` to run the TCR cycle.
    * Tests must pass for changes to be committed.
    * Failed tests will result in an automatic revert of changes.
5.  **Data Management**: Initially in-memory data structures. No external database is required for the scope of this kata.
6.  **Modularity**: Aim for a clean separation of concerns (e.g., service layer, domain objects).

---

### Development Steps (Aligned with Kata Requirements)

1.  **Core Service & Posting Feature (`TimelineService`)**
    * Set up the basic project structure for Kotlin with Maven.
    * Define a `TimelineService` class.
    * Implement the "Posting" feature:
        * Allow a user (e.g., "Alice") to publish a message.
        * Store the message associated with Alice's timeline.
        * Ensure messages have content and a timestamp (implicitly or explicitly).

2.  **Reading Feature**
    * Implement the "Reading" feature:
        * Allow a user (e.g., "Bob") to view another user's (e.g., "Alice") timeline.
        * Timelines should display messages in reverse chronological order (most recent first).

3.  **Following Feature**
    * Implement the "Following" feature:
        * Allow a user (e.g., "Charlie") to subscribe to another user's (e.g., "Alice") timeline.
        * Allow a user ("Charlie") to subscribe to multiple users' timelines (e.g., "Alice" and "Bob").
        * Provide a way for "Charlie" to view an aggregated wall/feed of messages from all subscribed timelines, ordered reverse chronologically.

4.  **Mentions Feature**
    * Implement the "Mentions" feature:
        * When a user posts a message containing "@username", this should be recognized as a mention.
        * (Optional extension, if time permits: The mentioned user could be notified or the message could appear on their "mentions" feed, but the core is recognizing it). For this kata, simply ensuring the mention is part of the message content and can be processed or displayed appropriately is sufficient.

5.  **Links Feature**
    * Implement the "Links" feature:
        * When a user posts a message containing a URL (e.g., "http://example.com"), this should be recognized as a link.
        * (Optional extension, if time permits: Links could be rendered as clickable, but the core is recognizing and storing them).

6.  **Direct Messages Feature**
    * Implement the "Direct Messages" feature:
        * Allow a user (e.g., "Mallory") to send a private message to another user (e.g., "Alice").
        * This message should appear on Alice's timeline but be visible *only* to Alice (and Mallory, the sender). It should not appear on Alice's public timeline viewable by others, nor on any aggregated feeds of users following Alice.

---

### Project Workflow & TCR Interaction

1.  **Bootstrap**:
    * Ensure Kotlin, Maven, and Java JDK are correctly set up.
    * Ensure `tcr.sh` is executable and configured.
2.  **Iterative Development (per feature/requirement)**:
    * **Write a Test**: Create a new failing test case in a relevant `*Test.kt` file that defines the next small piece of behavior for the current requirement.
    * **Implement**: Write the minimal production code in `src/main/kotlin` to make the test pass.
    * **TCR Cycle**: Execute `./tcr.sh -c`.
        * If tests pass: Changes are committed with a descriptive message (potentially AI-generated via the script).
        * If tests fail: Changes are reverted. Re-evaluate the test or implementation.
    * Repeat by writing the next test.
3.  **Refactoring**:
    * Refactor code as needed *after* tests are green and changes are committed. Subsequent refactoring steps should also follow the write-test (if applicable for observable behavior change) / implement / TCR cycle (`./tcr.sh -c`).

---

### Hand-Off Notes

* **Language/Tools**: Kotlin, Maven, JUnit 5.
* **Environment**: Assumes a Unix-like environment for `tcr.sh`.
* **Focus**: Adherence to TCR and incremental implementation of the defined features.
* **Git**: do not execute any git commands. The `tcr.sh` script will do all the work

This specification provides the guiding framework for developing the Social Network Kata in Kotlin.