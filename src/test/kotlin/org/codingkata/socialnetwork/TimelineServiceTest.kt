package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test

class TimelineServiceTest {

    @Test
    fun `alice can publish messages to her personal timeline`() {
        val service = TimelineService()
        service.    publishMessage(authorUserId = "alice", content= "Hello from alice")
    }
}