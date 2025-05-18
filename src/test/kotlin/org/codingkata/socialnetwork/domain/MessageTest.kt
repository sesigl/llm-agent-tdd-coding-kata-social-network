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
}
