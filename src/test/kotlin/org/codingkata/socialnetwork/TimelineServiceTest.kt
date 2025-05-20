package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}
