package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.TimelineRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineServiceTest {
    @Test
    fun `should post message to user timeline`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")
        val content = "Hello, world!"

        service.postMessage(userId, content)

        val messages = repository.getMessagesFor(userId)
        assertEquals(1, messages.size)
        assertEquals(content, messages[0].content)
        assertEquals(userId, messages[0].author)
    }

    @Test
    fun `should not allow empty message content`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        try {
            service.postMessage(userId, "")
            assert(false) { "Expected exception was not thrown" }
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }

        val messages = repository.getMessagesFor(userId)
        assertEquals(0, messages.size)
    }

    @Test
    fun `should not allow blank message content`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        try {
            service.postMessage(userId, "   ")
            assert(false) { "Expected exception was not thrown" }
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }

        val messages = repository.getMessagesFor(userId)
        assertEquals(0, messages.size)
    }

    @Test
    fun `should retrieve timeline for user`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")
        val message1 = Message.create("First message", userId, Timestamp(Instant.parse("2023-01-01T12:00:00Z")))
        val message2 = Message.create("Second message", userId, Timestamp(Instant.parse("2023-01-01T12:05:00Z")))
        repository.addMessage(message1)
        repository.addMessage(message2)

        val timeline = service.getTimeline(userId)

        assertEquals(2, timeline.size)
        // Newest messages should come first
        assertEquals(message2, timeline[0])
        assertEquals(message1, timeline[1])
    }

    @Test
    fun `should return empty list when user has no messages`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        val timeline = service.getTimeline(userId)

        assertTrue(timeline.isEmpty())
    }

    @Test
    fun `should retrieve only messages for requested user`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val alice = UserId("alice")
        val bob = UserId("bob")
        val aliceMessage = Message.create("Alice's message", alice)
        val bobMessage = Message.create("Bob's message", bob)
        repository.addMessage(aliceMessage)
        repository.addMessage(bobMessage)

        val aliceTimeline = service.getTimeline(alice)
        val bobTimeline = service.getTimeline(bob)

        assertEquals(1, aliceTimeline.size)
        assertEquals(aliceMessage, aliceTimeline[0])

        assertEquals(1, bobTimeline.size)
        assertEquals(bobMessage, bobTimeline[0])
    }
}
