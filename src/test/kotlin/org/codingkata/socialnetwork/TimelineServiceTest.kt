package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    @Test
    fun `alice can publish messages to her personal timeline`() {
        val service = TimelineService()
        service.postMessage(authorUserId = "alice", messageContent = "message")

        val messages = service.getAllMessages(authorUserId = "alice")

        assertEquals(listOf("message"), messages)
    }
}