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

    @Test
    fun `timeline should display messages in chronological order with newest first`() {
        // Arrange
        val timelineService = TimelineService()
        val username = "Alice"
        val message1 = "First message"
        val message2 = "Second message"
        val message3 = "Third message"

        // Act
        timelineService.post(username, message1)
        Thread.sleep(10) // Ensure different timestamps
        timelineService.post(username, message2)
        Thread.sleep(10) // Ensure different timestamps
        timelineService.post(username, message3)

        val messagesWithTime = timelineService.getTimelineWithTime(username)
        val messages = timelineService.getTimeline(username)

        // Assert
        assertEquals(3, messagesWithTime.size)
        assertEquals(3, messages.size)

        // Check that the messages are in reverse chronological order (newest first)
        assertEquals(message3, messages[0])
        assertEquals(message2, messages[1])
        assertEquals(message1, messages[2])

        // Verify timestamps are in descending order
        assert(messagesWithTime[0].second > messagesWithTime[1].second)
        assert(messagesWithTime[1].second > messagesWithTime[2].second)
    }
}
