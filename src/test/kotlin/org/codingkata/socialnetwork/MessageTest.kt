package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class MessageTest {
    @Test
    fun `creating a message with content, author and timestamp`() {
        val author = User("alice")
        val timestamp = Instant.now()
        val content = "Hello, world!"

        val message = Message(content, author, timestamp)

        assertEquals(content, message.content)
        assertEquals(author, message.author)
        assertEquals(timestamp, message.timestamp)
    }

    @Test
    fun `messages with same content, author and timestamp are equal`() {
        val author = User("alice")
        val timestamp = Instant.now()
        val content = "Hello, world!"

        val message1 = Message(content, author, timestamp)
        val message2 = Message(content, author, timestamp)

        assertEquals(message1, message2)
        assertEquals(message1.hashCode(), message2.hashCode())
    }

    @Test
    fun `messages with different content are not equal`() {
        val author = User("alice")
        val timestamp = Instant.now()

        val message1 = Message("Hello, world!", author, timestamp)
        val message2 = Message("Goodbye, world!", author, timestamp)

        assertNotEquals(message1, message2)
    }

    @Test
    fun `messages with different authors are not equal`() {
        val timestamp = Instant.now()
        val content = "Hello, world!"

        val message1 = Message(content, User("alice"), timestamp)
        val message2 = Message(content, User("bob"), timestamp)

        assertNotEquals(message1, message2)
    }

    @Test
    fun `messages with different timestamps are not equal`() {
        val author = User("alice")
        val content = "Hello, world!"

        val message1 = Message(content, author, Instant.ofEpochSecond(1000))
        val message2 = Message(content, author, Instant.ofEpochSecond(2000))

        assertNotEquals(message1, message2)
    }

    @Test
    fun `message can determine if it was posted before another message`() {
        val author = User("alice")
        val content = "Hello, world!"
        val earlier = Instant.now().minus(1, ChronoUnit.HOURS)
        val later = Instant.now()

        val earlierMessage = Message(content, author, earlier)
        val laterMessage = Message(content, author, later)

        assertTrue(earlierMessage.isPostedBefore(laterMessage))
        assertFalse(laterMessage.isPostedBefore(earlierMessage))
    }

    @Test
    fun `message can determine if it was posted by a specific user`() {
        val alice = User("alice")
        val bob = User("bob")
        val content = "Hello, world!"
        val timestamp = Instant.now()

        val message = Message(content, alice, timestamp)

        assertTrue(message.isPostedBy(alice))
        assertFalse(message.isPostedBy(bob))
    }

    @Test
    fun `message can determine how old it is compared to now`() {
        val author = User("alice")
        val content = "Hello, world!"
        val oneHourAgo = Instant.now().minus(1, ChronoUnit.HOURS)

        val message = Message(content, author, oneHourAgo)

        val ageInSeconds = message.getAgeInSeconds()

        assertTrue(ageInSeconds >= 3600 && ageInSeconds < 3660) // Allow small delta for test execution time
    }
}
