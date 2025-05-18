package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineTest {
    @Test
    fun `should create an empty timeline for user`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)
        val timeline = Timeline(timelineId)

        assertEquals(timelineId, timeline.id)
        assertTrue(timeline.getMessages().isEmpty())
    }

    @Test
    fun `should add message to timeline`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)
        val timeline = Timeline(timelineId)
        val message = Message.create("Hello, world!", userId)

        timeline.addMessage(message)

        assertEquals(1, timeline.getMessages().size)
        assertEquals(message, timeline.getMessages()[0])
    }

    @Test
    fun `should return messages in chronological order newest first`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)
        val timeline = Timeline(timelineId)

        val message1 =
            Message.create(
                "First message",
                userId,
                Timestamp(Instant.parse("2023-01-01T12:00:00Z")),
            )
        val message2 =
            Message.create(
                "Second message",
                userId,
                Timestamp(Instant.parse("2023-01-01T12:10:00Z")),
            )
        val message3 =
            Message.create(
                "Third message",
                userId,
                Timestamp(Instant.parse("2023-01-01T12:05:00Z")),
            )

        timeline.addMessage(message1)
        timeline.addMessage(message2)
        timeline.addMessage(message3)

        val messages = timeline.getMessages()
        assertEquals(3, messages.size)
        assertEquals(message2, messages[0]) // newest first
        assertEquals(message3, messages[1])
        assertEquals(message1, messages[2])
    }

    @Test
    fun `should query messages with limit`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)
        val timeline = Timeline(timelineId)

        val timestamps =
            listOf(
                "2023-01-01T12:00:00Z",
                "2023-01-01T12:05:00Z",
                "2023-01-01T12:10:00Z",
                "2023-01-01T12:15:00Z",
                "2023-01-01T12:20:00Z",
            )

        timestamps.forEach { time ->
            timeline.addMessage(
                Message.create(
                    "Message at $time",
                    userId,
                    Timestamp(Instant.parse(time)),
                ),
            )
        }

        val query = TimelineQuery.withLimit(3)
        val filteredMessages = timeline.getMessages(query)

        assertEquals(3, filteredMessages.size)
    }

    @Test
    fun `should query messages before timestamp`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)
        val timeline = Timeline(timelineId)

        val timestamps =
            mapOf(
                "2023-01-01T12:00:00Z" to "Message 1",
                "2023-01-01T12:05:00Z" to "Message 2",
                "2023-01-01T12:10:00Z" to "Message 3",
                "2023-01-01T12:15:00Z" to "Message 4",
            )

        timestamps.forEach { (time, content) ->
            timeline.addMessage(
                Message.create(
                    content,
                    userId,
                    Timestamp(Instant.parse(time)),
                ),
            )
        }

        val cutoffTime = Timestamp(Instant.parse("2023-01-01T12:10:00Z"))
        val query = TimelineQuery.before(cutoffTime)
        val filteredMessages = timeline.getMessages(query)

        assertEquals(2, filteredMessages.size)
        assertTrue(filteredMessages.all { it.timestamp < cutoffTime })
    }

    @Test
    fun `should query messages after timestamp`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)
        val timeline = Timeline(timelineId)

        val timestamps =
            mapOf(
                "2023-01-01T12:00:00Z" to "Message 1",
                "2023-01-01T12:05:00Z" to "Message 2",
                "2023-01-01T12:10:00Z" to "Message 3",
                "2023-01-01T12:15:00Z" to "Message 4",
            )

        timestamps.forEach { (time, content) ->
            timeline.addMessage(
                Message.create(
                    content,
                    userId,
                    Timestamp(Instant.parse(time)),
                ),
            )
        }

        val cutoffTime = Timestamp(Instant.parse("2023-01-01T12:05:00Z"))
        val query = TimelineQuery.after(cutoffTime)
        val filteredMessages = timeline.getMessages(query)

        assertEquals(2, filteredMessages.size)
        assertTrue(filteredMessages.all { it.timestamp > cutoffTime })
    }

    @Test
    fun `should add message only for the timeline owner`() {
        val aliceId = UserId("alice")
        val bobId = UserId("bob")
        val timelineId = TimelineId(aliceId)
        val timeline = Timeline(timelineId)

        val aliceMessage = Message.create("Alice's message", aliceId)
        val bobMessage = Message.create("Bob's message", bobId)

        timeline.addMessage(aliceMessage)
        timeline.addMessage(bobMessage)

        assertEquals(1, timeline.getMessages().size)
        assertEquals(aliceMessage, timeline.getMessages()[0])
    }
}
