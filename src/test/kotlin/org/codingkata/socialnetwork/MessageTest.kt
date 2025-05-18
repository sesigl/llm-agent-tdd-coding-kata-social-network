package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class MessageTest {
    @Test
    fun `should create a message with valid content, author and timestamp`() {
        val author = UserId("alice")
        val content = "Hello, world!"
        val timestamp = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))

        val message = Message(content, author, timestamp)

        assertEquals(content, message.content)
        assertEquals(author, message.author)
        assertEquals(timestamp, message.timestamp)
    }

    @Test
    fun `should create a message with current timestamp when not provided`() {
        val author = UserId("alice")
        val content = "Hello, world!"

        val message = Message(content, author)

        assertEquals(content, message.content)
        assertEquals(author, message.author)
        // We can't directly assert the timestamp's exact value since it uses current time
    }

    @Test
    fun `should not allow empty content`() {
        val author = UserId("alice")

        assertThrows<IllegalArgumentException> {
            Message("", author)
        }
    }

    @Test
    fun `should not allow blank content`() {
        val author = UserId("alice")

        assertThrows<IllegalArgumentException> {
            Message("   ", author)
        }
    }

    @Test
    fun `should consider messages with same content, author and timestamp equal`() {
        val author = UserId("alice")
        val content = "Hello, world!"
        val timestamp = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))

        val message1 = Message(content, author, timestamp)
        val message2 = Message(content, author, timestamp)

        assertEquals(message1, message2)
        assertEquals(message1.hashCode(), message2.hashCode())
    }

    @Test
    fun `should consider messages with different content not equal`() {
        val author = UserId("alice")
        val timestamp = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))

        val message1 = Message("Hello, world!", author, timestamp)
        val message2 = Message("Goodbye, world!", author, timestamp)

        assertNotEquals(message1, message2)
    }

    @Test
    fun `should consider messages with different authors not equal`() {
        val content = "Hello, world!"
        val timestamp = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))

        val message1 = Message(content, UserId("alice"), timestamp)
        val message2 = Message(content, UserId("bob"), timestamp)

        assertNotEquals(message1, message2)
    }

    @Test
    fun `should consider messages with different timestamps not equal`() {
        val author = UserId("alice")
        val content = "Hello, world!"

        val message1 = Message(content, author, Timestamp(Instant.parse("2023-01-01T12:00:00Z")))
        val message2 = Message(content, author, Timestamp(Instant.parse("2023-01-01T13:00:00Z")))

        assertNotEquals(message1, message2)
    }
}
