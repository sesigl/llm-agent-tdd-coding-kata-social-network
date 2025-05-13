package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    @Test
    fun `alice and bob can publish messages to their personal timeline`() {
        val service = TimelineService()
        service.postMessage(authorUserId = "alice", messageContent = "message from alice")
        service.postMessage(authorUserId = "bob", messageContent = "message from bob")

        val messages = service.getAllMessages(authorUserId = "alice")

        assertEquals(listOf("message from alice"), messages)
    }
}