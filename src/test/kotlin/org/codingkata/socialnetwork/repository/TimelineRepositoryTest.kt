package org.codingkata.socialnetwork.repository

import org.codingkata.socialnetwork.Message
import org.codingkata.socialnetwork.Timestamp
import org.codingkata.socialnetwork.UserId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineRepositoryTest {
    @Test
    fun `should store message for user`() {
        val repository = TimelineRepository()
        val userId = UserId("alice")
        val message = Message("Hello, world!", userId)

        repository.addMessage(message)

        val messages = repository.getMessagesFor(userId)
        assertEquals(1, messages.size)
        assertEquals(message, messages[0])
    }

    @Test
    fun `should return empty list for user with no messages`() {
        val repository = TimelineRepository()
        val userId = UserId("alice")

        val messages = repository.getMessagesFor(userId)

        assertTrue(messages.isEmpty())
    }

    @Test
    fun `should store multiple messages for same user`() {
        val repository = TimelineRepository()
        val userId = UserId("alice")
        val message1 = Message("Hello, world!", userId, Timestamp(Instant.parse("2023-01-01T12:00:00Z")))
        val message2 = Message("Hello again!", userId, Timestamp(Instant.parse("2023-01-01T12:01:00Z")))

        repository.addMessage(message1)
        repository.addMessage(message2)

        val messages = repository.getMessagesFor(userId)
        assertEquals(2, messages.size)
        assertTrue(messages.contains(message1))
        assertTrue(messages.contains(message2))
    }

    @Test
    fun `should keep messages separate for different users`() {
        val repository = TimelineRepository()
        val alice = UserId("alice")
        val bob = UserId("bob")
        val aliceMessage = Message("Hello from Alice!", alice)
        val bobMessage = Message("Hello from Bob!", bob)

        repository.addMessage(aliceMessage)
        repository.addMessage(bobMessage)

        val aliceMessages = repository.getMessagesFor(alice)
        val bobMessages = repository.getMessagesFor(bob)

        assertEquals(1, aliceMessages.size)
        assertEquals(aliceMessage, aliceMessages[0])

        assertEquals(1, bobMessages.size)
        assertEquals(bobMessage, bobMessages[0])
    }
}
