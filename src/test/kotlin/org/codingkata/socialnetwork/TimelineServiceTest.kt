package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.TimelineRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class TimelineServiceTest {
    private lateinit var repository: TimelineRepository
    private lateinit var service: TimelineService
    private val alice = UserId("alice")
    private val bob = UserId("bob")

    @BeforeEach
    fun setup() {
        repository = TimelineRepository()
        service = TimelineService(repository)
    }

    @Test
    fun `should post message to user timeline`() {
        val content = "Hello, world!"

        service.postMessage(alice, content)

        val messages = repository.getMessagesFor(alice)
        assertEquals(1, messages.size)
        assertEquals(content, messages[0].content)
        assertEquals(alice, messages[0].author)
    }

    @Test
    fun `should not allow empty message content`() {
        assertThrows<IllegalArgumentException> {
            service.postMessage(alice, "")
        }

        val messages = repository.getMessagesFor(alice)
        assertEquals(0, messages.size)
    }

    @Test
    fun `should not allow blank message content`() {
        assertThrows<IllegalArgumentException> {
            service.postMessage(alice, "   ")
        }

        val messages = repository.getMessagesFor(alice)
        assertEquals(0, messages.size)
    }

    @Test
    fun `should retrieve timeline for user`() {
        val message1 = Message.create("First message", alice, Timestamp(Instant.parse("2023-01-01T12:00:00Z")))
        val message2 = Message.create("Second message", alice, Timestamp(Instant.parse("2023-01-01T12:05:00Z")))
        repository.addMessage(message1)
        repository.addMessage(message2)

        val timeline = service.getTimeline(alice)

        assertEquals(2, timeline.size)
        // Newest messages should come first
        assertEquals(message2, timeline[0])
        assertEquals(message1, timeline[1])
    }

    @Test
    fun `should return empty list when user has no messages`() {
        val timeline = service.getTimeline(alice)

        assertTrue(timeline.isEmpty())
    }

    @Test
    fun `should retrieve only messages for requested user`() {
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
