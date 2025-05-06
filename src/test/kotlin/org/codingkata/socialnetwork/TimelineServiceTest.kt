package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    @Test
    fun `alice can publish messages to her personal timeline`() {
        val service = TimelineService()
        service.publishMessage(authorUserId = "alice", content = "Hello from alice")

        assertEquals(listOf("Hello from alice"), service.getMessages(userId = "alice"))
    }
}