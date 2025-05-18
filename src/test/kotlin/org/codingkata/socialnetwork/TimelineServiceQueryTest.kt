package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.TimelineRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class TimelineServiceQueryTest {
    @Test
    fun `should limit number of messages returned`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        val timestamps =
            listOf(
                "2023-01-01T12:00:00Z",
                "2023-01-01T12:05:00Z",
                "2023-01-01T12:10:00Z",
                "2023-01-01T12:15:00Z",
                "2023-01-01T12:20:00Z",
            ).reversed()

        val messages =
            timestamps.mapIndexed { index, time ->
                Message.create("Message ${index + 1}", userId, Timestamp(Instant.parse(time)))
            }

        messages.forEach { repository.addMessage(it) }

        val query = TimelineQuery.withLimit(3)
        val timeline = service.getTimeline(userId, query)

        assertEquals(3, timeline.size)
        assertEquals(messages[0], timeline[0])
        assertEquals(messages[1], timeline[1])
        assertEquals(messages[2], timeline[2])
    }

    @Test
    fun `should filter messages before timestamp`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        val timestamps =
            mapOf(
                "message1" to "2023-01-01T12:00:00Z",
                "message2" to "2023-01-01T12:05:00Z",
                "message3" to "2023-01-01T12:10:00Z",
                "message4" to "2023-01-01T12:15:00Z",
            )

        val messages =
            timestamps.map { (content, time) ->
                Message.create(content, userId, Timestamp(Instant.parse(time)))
            }
        messages.forEach { repository.addMessage(it) }

        val cutoffTime = Timestamp(Instant.parse("2023-01-01T12:10:00Z"))
        val query = TimelineQuery.before(cutoffTime)
        val timeline = service.getTimeline(userId, query)

        assertEquals(2, timeline.size)
        assertEquals("message2", timeline[0].content)
        assertEquals("message1", timeline[1].content)
    }

    @Test
    fun `should filter messages after timestamp`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        val timestamps =
            mapOf(
                "message1" to "2023-01-01T12:00:00Z",
                "message2" to "2023-01-01T12:05:00Z",
                "message3" to "2023-01-01T12:10:00Z",
                "message4" to "2023-01-01T12:15:00Z",
            )

        val messages =
            timestamps.map { (content, time) ->
                Message.create(content, userId, Timestamp(Instant.parse(time)))
            }
        messages.forEach { repository.addMessage(it) }

        val cutoffTime = Timestamp(Instant.parse("2023-01-01T12:05:00Z"))
        val query = TimelineQuery.after(cutoffTime)
        val timeline = service.getTimeline(userId, query)

        assertEquals(2, timeline.size)
        assertEquals("message4", timeline[0].content)
        assertEquals("message3", timeline[1].content)
    }

    @Test
    fun `should filter messages between timestamps`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        val timestamps =
            mapOf(
                "message1" to "2023-01-01T12:00:00Z",
                "message2" to "2023-01-01T12:05:00Z",
                "message3" to "2023-01-01T12:10:00Z",
                "message4" to "2023-01-01T12:15:00Z",
            )

        val messages =
            timestamps.map { (content, time) ->
                Message.create(content, userId, Timestamp(Instant.parse(time)))
            }
        messages.forEach { repository.addMessage(it) }

        val afterTime = Timestamp(Instant.parse("2023-01-01T12:05:00Z"))
        val beforeTime = Timestamp(Instant.parse("2023-01-01T12:15:00Z"))
        val query = TimelineQuery.between(afterTime, beforeTime)
        val timeline = service.getTimeline(userId, query)

        assertEquals(1, timeline.size)
        assertEquals("message3", timeline[0].content)
    }

    @Test
    fun `should combine limit with time filtering`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        val timestamps =
            listOf(
                "2023-01-01T12:00:00Z",
                "2023-01-01T12:05:00Z",
                "2023-01-01T12:10:00Z",
                "2023-01-01T12:15:00Z",
                "2023-01-01T12:20:00Z",
            )

        val messages =
            timestamps.mapIndexed { index, time ->
                Message.create("Message ${index + 1}", userId, Timestamp(Instant.parse(time)))
            }

        messages.forEach { repository.addMessage(it) }

        val query =
            TimelineQuery
                .builder()
                .after(Timestamp(Instant.parse("2023-01-01T12:05:00Z")))
                .limit(2)
                .build()

        val timeline = service.getTimeline(userId, query)

        assertEquals(2, timeline.size)
        assertEquals("Message 5", timeline[0].content)
        assertEquals("Message 4", timeline[1].content)
    }

    @Test
    fun `should return empty list when no messages match query`() {
        val repository = TimelineRepository()
        val service = TimelineService(repository)
        val userId = UserId("alice")

        repository.addMessage(
            Message.create(
                "Hello",
                userId,
                Timestamp(Instant.parse("2023-01-01T12:00:00Z")),
            ),
        )

        val query = TimelineQuery.before(Timestamp(Instant.parse("2022-01-01T00:00:00Z")))
        val timeline = service.getTimeline(userId, query)

        assertTrue(timeline.isEmpty())
    }
}
