package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.time.Instant

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
}
