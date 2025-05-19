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
}
