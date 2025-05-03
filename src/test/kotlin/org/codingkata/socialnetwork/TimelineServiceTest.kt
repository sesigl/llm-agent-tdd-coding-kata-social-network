package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    private lateinit var timelineService: TimelineService

    @BeforeEach
    fun setUp() {
        timelineService = TimelineService()
    }

    @Test
    fun `alice can publish 2 messages to a personal timeline`() {
        val messages = publishAndGetMessages(listOf("message 1", "message 2"), "alice")

        assertEquals(listOf("message 1", "message 2"), messages)
    }

    @Test
    fun `multiple users can publish messages`() {
        val messagesFromAlice = publishAndGetMessages("message from alice", "alice")
        val messagesFromBob = publishAndGetMessages("message from bob", "bob")

        assertEquals(listOf("message from bob"), messagesFromBob)
        assertEquals(listOf("message from alice"), messagesFromAlice)
    }

    private fun publishAndGetMessages(messageContent: String, userId: String): List<String> {
        return publishAndGetMessages(listOf(messageContent), userId)
    }

    private fun publishAndGetMessages(messageContents: List<String>, userId: String): List<String> {
        messageContents.forEach {
            timelineService.publishMessage(
                messageContent = it,
                userId = userId
            )
        }

        return timelineService.getAllMessages(
            userId = userId,
        )
    }
}