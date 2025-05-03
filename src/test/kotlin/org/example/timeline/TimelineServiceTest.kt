package org.example.timeline

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TimelineServiceTest {

    private lateinit var timelineService: TimelineService

    @BeforeEach
    fun setUp() {
        timelineService = TimelineService()
    }

    @Test
    fun `alice can publish messages to her timeline`() {
        timelineService.publish(
            ownerUserId = "alice",
            messageContent = "hi"
        )

        val timelineMessages = timelineService.getAllMessages(
            ownerUserId = "alice",
            requestedByUserId = "alice"
        )

        assertEquals(listOf("hi"), timelineMessages)
    }

    @Test
    fun `by default bob can not view Alice's timeline`() {
        assertThrows<Timeline.NotAllowed> {
            timelineService.getAllMessages(ownerUserId = "alice", requestedByUserId = "bob")
        }
    }

    @Test
    fun `charlie can subscribe to alice's timeline updates`() {
        timelineService.subscribeTo(ownerUserId = "charlie", subscriberUserId ="alice")
        timelineService.publish(ownerUserId = "alice", messageContent = "messageFromAlice")

        assertEquals(listOf("messageFromAlice"), timelineService.getAllMessages(ownerUserId = "charlie"))
    }

    @Test
    fun `bob can link to charlie in a message`() {
        timelineService.publish(ownerUserId = "bob", messageContent = "hi @charlie")
        assertEquals(listOf("hi @<user:charlie>"), timelineService.getAllMessages(ownerUserId = "charlie"))
    }

    @Test
    fun `alice can link to a clickable web resource in a message`() {
        timelineService.publish(ownerUserId = "alice", messageContent = "link: www.google.de")
        assertEquals(listOf("link: <url:https://www.google.de>"), timelineService.getAllMessages(ownerUserId = "alice"))
    }
}