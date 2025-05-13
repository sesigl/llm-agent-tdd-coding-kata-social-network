package org.codingkata.socialnetwork.org.codingkata.socialnetwork

import org.codingkata.socialnetwork.TimelineService
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TimelineServiceTest {

    /**
     * Notes
     *
     * - another user can not read a users timeline if timeline is set to private ?
     * - when linked in a message, does it automatically appear in another timeline ?
     */
    @Test
    fun `alice and bob can publish messages to their personal timeline`() {
        val service = TimelineService()
        service.postMessage(authorUserId = "alice", messageContent = "message from alice")
        service.postMessage(authorUserId = "bob", messageContent = "message from bob")

        val messages = service.getAllMessages(authorUserId = "alice")

        assertEquals(listOf("message from alice"), messages)
    }

    @Test
    fun `bob can view alice's timeline`() {
        val service = TimelineService()

        service.postMessage(authorUserId = "alice", messageContent = "message from alice")

        val messages = service.getAllMessages(authorUserId = "alice", requesterUserId = "bob")
        assertEquals(listOf("message from alice"), messages)
    }

    @Test
    fun `charlie can subscribe to alice's and bob's timeline`() {
        val service = TimelineService()

        service.subscribe(subscriberUserId = "charlie", targetTimelineUserId = "alice")
        service.subscribe(subscriberUserId = "charlie", targetTimelineUserId = "bob")

        service.postMessage(authorUserId = "alice", messageContent = "message from alice")
        service.postMessage(authorUserId = "bob", messageContent = "message from bob")

        val messages = service.getAllMessages(authorUserId = "charlie")
        assertEquals(listOf("message from alice", "message from bob"), messages)
    }

    @Test
    fun `bob can link to charlie in a message using @`() {
        val service = TimelineService()

        service.postMessage(authorUserId = "bob", messageContent = "message from @bob about @charlie!")

        val messages = service.getAllMessages(authorUserId = "bob")
        assertEquals(listOf("message from <user:bob> about <user:charlie>!"), messages)
    }
}