package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

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

    @Test
    fun `multiple users can publish messages`() {
        val timelineService = TimelineService()

        timelineService.publishMessage(
            messageContent = "message from alice",
            userId = "alice"
        )
        val messagesFromAlice = timelineService.getAllMessages(
            userId = "alice",
        )

        timelineService.publishMessage(
            messageContent = "message from bob",
            userId = "bob"
        )
        val messagesFromBob = timelineService.getAllMessages(
            userId = "bob",
        )

        assertEquals(listOf("message from bob"), messagesFromBob)
        assertEquals(listOf("message from alice"), messagesFromAlice)
    }
}