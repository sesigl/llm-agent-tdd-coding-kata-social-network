package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineServiceTest {
    private lateinit var timelineService: TimelineService

    @BeforeEach
    fun setup() {
        timelineService = TimelineService()
    }

    @Test
    fun `user can post a message to their timeline`() {
        val user = User("alice")
        val content = "Hello, world!"

        timelineService.postMessage(user, content)

        val timeline = timelineService.getTimelineForUser(user)
        assertEquals(1, timeline.size)
        assertEquals(content, timeline[0].content)
        assertEquals(user, timeline[0].author)
    }

    @Test
    fun `multiple messages from same user are stored in timeline`() {
        val user = User("bob")

        timelineService.postMessage(user, "First message")
        timelineService.postMessage(user, "Second message")

        val timeline = timelineService.getTimelineForUser(user)
        assertEquals(2, timeline.size)
        assertEquals("First message", timeline[0].content)
        assertEquals("Second message", timeline[1].content)
    }

    @Test
    fun `messages are timestamped when posted`() {
        val user = User("charlie")
        val beforePost = Instant.now().minusMillis(100)

        timelineService.postMessage(user, "Time-sensitive message")

        val timeline = timelineService.getTimelineForUser(user)
        val messageTimestamp = timeline[0].timestamp
        val afterPost = Instant.now().plusMillis(100)

        assertTrue(messageTimestamp.isAfter(beforePost) || messageTimestamp.equals(beforePost))
        assertTrue(messageTimestamp.isBefore(afterPost) || messageTimestamp.equals(afterPost))
    }

    @Test
    fun `user timeline only contains their own messages`() {
        val alice = User("alice")
        val bob = User("bob")

        timelineService.postMessage(alice, "Alice's message")
        timelineService.postMessage(bob, "Bob's message")

        val aliceTimeline = timelineService.getTimelineForUser(alice)
        assertEquals(1, aliceTimeline.size)
        assertEquals("Alice's message", aliceTimeline[0].content)

        val bobTimeline = timelineService.getTimelineForUser(bob)
        assertEquals(1, bobTimeline.size)
        assertEquals("Bob's message", bobTimeline[0].content)
    }

    @Test
    fun `timeline can be retrieved in reverse chronological order`() {
        val user = User("david")

        // Post messages with a small delay to ensure different timestamps
        timelineService.postMessage(user, "First message")
        Thread.sleep(10)
        timelineService.postMessage(user, "Second message")
        Thread.sleep(10)
        timelineService.postMessage(user, "Third message")

        val timeline = timelineService.getTimelineForUserInReverseChronologicalOrder(user)
        assertEquals(3, timeline.size)
        assertEquals("Third message", timeline[0].content)
        assertEquals("Second message", timeline[1].content)
        assertEquals("First message", timeline[2].content)
    }

    @Test
    fun `empty timeline is returned for user who hasn't posted`() {
        val user = User("emptyUser")

        val timeline = timelineService.getTimelineForUserInReverseChronologicalOrder(user)

        assertTrue(timeline.isEmpty())
    }

    @Test
    fun `user can follow another user`() {
        val alice = User("alice")
        val bob = User("bob")

        timelineService.followUser(alice, bob)

        val followedUsers = timelineService.getFollowedUsers(alice)
        assertEquals(1, followedUsers.size)
        assertEquals(bob, followedUsers[0])
    }

    @Test
    fun `user can follow multiple users`() {
        val alice = User("alice")
        val bob = User("bob")
        val charlie = User("charlie")

        timelineService.followUser(alice, bob)
        timelineService.followUser(alice, charlie)

        val followedUsers = timelineService.getFollowedUsers(alice)
        assertEquals(2, followedUsers.size)
        assertTrue(followedUsers.contains(bob))
        assertTrue(followedUsers.contains(charlie))
    }

    @Test
    fun `user cannot follow the same user twice`() {
        val alice = User("alice")
        val bob = User("bob")

        timelineService.followUser(alice, bob)
        timelineService.followUser(alice, bob)

        val followedUsers = timelineService.getFollowedUsers(alice)
        assertEquals(1, followedUsers.size)
    }

    @Test
    fun `aggregated timeline contains messages from followed users`() {
        val alice = User("alice")
        val bob = User("bob")
        val charlie = User("charlie")

        timelineService.postMessage(alice, "Alice's message")
        timelineService.postMessage(bob, "Bob's message")
        timelineService.postMessage(charlie, "Charlie's message")

        timelineService.followUser(alice, bob)
        timelineService.followUser(alice, charlie)

        val aggregatedTimeline = timelineService.getAggregatedTimeline(alice)

        assertEquals(3, aggregatedTimeline.size)
        assertTrue(aggregatedTimeline.any { it.content == "Alice's message" })
        assertTrue(aggregatedTimeline.any { it.content == "Bob's message" })
        assertTrue(aggregatedTimeline.any { it.content == "Charlie's message" })
    }

    @Test
    fun `aggregated timeline is sorted by timestamp (newest first)`() {
        val alice = User("alice")
        val bob = User("bob")
        val charlie = User("charlie")

        // Follow users first to ensure all messages are included
        timelineService.followUser(alice, bob)
        timelineService.followUser(alice, charlie)

        // Post messages with delays to ensure different timestamps
        timelineService.postMessage(bob, "First message")
        Thread.sleep(10)
        timelineService.postMessage(charlie, "Second message")
        Thread.sleep(10)
        timelineService.postMessage(alice, "Third message")

        val aggregatedTimeline = timelineService.getAggregatedTimeline(alice)

        assertEquals(3, aggregatedTimeline.size)
        assertEquals("Third message", aggregatedTimeline[0].content)
        assertEquals("Second message", aggregatedTimeline[1].content)
        assertEquals("First message", aggregatedTimeline[2].content)
    }

    @Test
    fun `aggregated timeline includes user's own messages`() {
        val alice = User("alice")
        val bob = User("bob")

        timelineService.postMessage(alice, "Alice's message")
        timelineService.postMessage(bob, "Bob's message")

        timelineService.followUser(alice, bob)

        val aggregatedTimeline = timelineService.getAggregatedTimeline(alice)

        assertEquals(2, aggregatedTimeline.size)
        assertTrue(aggregatedTimeline.any { it.content == "Alice's message" })
        assertTrue(aggregatedTimeline.any { it.content == "Bob's message" })
    }
}
