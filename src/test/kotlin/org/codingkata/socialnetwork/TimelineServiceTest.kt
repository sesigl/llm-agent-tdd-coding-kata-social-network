package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    @Test
    fun `alice can publish messages to a personal timeline`() {
        val timelineService = TimelineService()
        timelineService.publishMessage(
            messageContent = "Happy coding",
            userId = "alice"
        )

        val messages = timelineService.getAllMessages(
            userId = "alice",
        )

        assertEquals(listOf("Happy coding"), messages)
    }

    @Test
    fun `alice can publish 2 messages to a personal timeline`() {
        val timelineService = TimelineService()

        timelineService.publishMessage(
            messageContent = "message 1",
            userId = "alice"
        )

        timelineService.publishMessage(
            messageContent = "message 2",
            userId = "alice"
        )

        val messages = timelineService.getAllMessages(
            userId = "alice",
        )

        assertEquals(listOf("message 1", "message 2"), messages)
    }
}