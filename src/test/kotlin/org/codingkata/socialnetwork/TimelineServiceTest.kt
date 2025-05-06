package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    @Test
    fun `alice can publish messages to her personal timeline`() {
        val service = TimelineService()
        service.publishMessage(ownerUserId = "alice", content = "Hello from alice")

        assertEquals(listOf("Hello from alice"), service.getMessages(userId = "alice"))
    }

    @Test
    fun `bob can view alice's timeline`() {
        val service = TimelineService()
        service.publishMessage(ownerUserId = "alice", content = "Hello from alice")

        assertEquals(listOf("Hello from alice"), service.getMessages(userId = "alice", requesterUserId = "bob"))
    }

    @Test
    fun `charlie can subscribe to alice's and bob's timeline`() {
        val service = TimelineService()

        service.subscribeTo(timelineUserId = "alice", subscriberUserId = "charlie")
        service.subscribeTo(timelineUserId = "bob", subscriberUserId = "charlie")

        service.publishMessage(ownerUserId = "alice", content = "Hello from alice")
        service.publishMessage(ownerUserId = "bob", content = "Hello from bob")

        assertEquals(listOf("Hello from alice", "Hello from bob"), service.getMessages(userId = "charlie"))
    }
}