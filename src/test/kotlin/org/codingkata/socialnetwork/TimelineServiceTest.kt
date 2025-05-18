package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.TimelineRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}
