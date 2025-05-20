package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineServiceTest {
    @Test
    fun `user can post message to timeline`() {
        // Arrange
        val timelineService = TimelineService()
        val username = "Alice"
        val message = "Hello, world!"

        // Act
        timelineService.post(username, message)
        val messages = timelineService.getMessages(username)

        // Assert
        assertEquals(1, messages.size)
        assertEquals(message, messages[0])
    }

    @Test
    fun `messages should have timestamps`() {
        // Arrange
        val timelineService = TimelineService()
        val username = "Alice"
        val message = "Hello, world!"
        val beforePost = Instant.now()

        // Act
        timelineService.post(username, message)
        val messages = timelineService.getMessagesWithTime(username)
        val afterPost = Instant.now()

        // Assert
        assertEquals(1, messages.size)
        val (content, timestamp) = messages[0]
        assertEquals(message, content)
        assertNotNull(timestamp)
        assert(timestamp >= beforePost) { "Timestamp should be after the post was created" }
        assert(timestamp <= afterPost) { "Timestamp should be before the assertion was executed" }
    }
}
