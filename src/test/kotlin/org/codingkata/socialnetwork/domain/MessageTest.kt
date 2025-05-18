package org.codingkata.socialnetwork.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MessageTest {
    @Test
    fun `should create a message with content and author`() {
        val user = User("alice")
        val content = "Hello, world!"

        val message = Message(content, user)

        assertEquals(content, message.content)
        assertEquals(user, message.author)
    }

    @Test
    fun `messages with same content and author should be equal`() {
        val user = User("alice")
        val content = "Hello, world!"

        val message1 = Message(content, user)
        val message2 = Message(content, user)

        assertEquals(message1, message2)
        assertEquals(message1.hashCode(), message2.hashCode())
    }
}
