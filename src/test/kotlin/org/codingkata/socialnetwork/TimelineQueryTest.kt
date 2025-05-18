package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class TimelineQueryTest {
    @Test
    fun `should create default query with no parameters`() {
        val query = TimelineQuery.default()
        assertNull(query.limit)
        assertNull(query.beforeTimestamp)
        assertNull(query.afterTimestamp)
    }

    @Test
    fun `should create query with limit`() {
        val limit = 10
        val query = TimelineQuery.withLimit(limit)
        assertEquals(limit, query.limit)
        assertNull(query.beforeTimestamp)
        assertNull(query.afterTimestamp)
    }

    @Test
    fun `should not allow negative limit`() {
        assertThrows<IllegalArgumentException> {
            TimelineQuery.withLimit(-1)
        }
    }

    @Test
    fun `should not allow zero limit`() {
        assertThrows<IllegalArgumentException> {
            TimelineQuery.withLimit(0)
        }
    }

    @Test
    fun `should create query with before timestamp`() {
        val timestamp = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))
        val query = TimelineQuery.before(timestamp)
        assertNull(query.limit)
        assertEquals(timestamp, query.beforeTimestamp)
        assertNull(query.afterTimestamp)
    }

    @Test
    fun `should create query with after timestamp`() {
        val timestamp = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))
        val query = TimelineQuery.after(timestamp)
        assertNull(query.limit)
        assertNull(query.beforeTimestamp)
        assertEquals(timestamp, query.afterTimestamp)
    }

    @Test
    fun `should create query with time range`() {
        val before = Timestamp(Instant.parse("2023-01-02T12:00:00Z"))
        val after = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))
        val query = TimelineQuery.between(after, before)
        assertNull(query.limit)
        assertEquals(before, query.beforeTimestamp)
        assertEquals(after, query.afterTimestamp)
    }

    @Test
    fun `should not allow invalid time range`() {
        val earlier = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))
        val later = Timestamp(Instant.parse("2023-01-02T12:00:00Z"))

        assertThrows<IllegalArgumentException> {
            TimelineQuery.between(later, earlier)
        }
    }

    @Test
    fun `should create combined query with all parameters`() {
        val limit = 5
        val before = Timestamp(Instant.parse("2023-01-02T12:00:00Z"))
        val after = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))

        val query =
            TimelineQuery
                .builder()
                .limit(limit)
                .before(before)
                .after(after)
                .build()

        assertEquals(limit, query.limit)
        assertEquals(before, query.beforeTimestamp)
        assertEquals(after, query.afterTimestamp)
    }
}
