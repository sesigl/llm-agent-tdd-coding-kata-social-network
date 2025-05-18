package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant

class TimestampTest {
    @Test
    fun `should create a Timestamp with current time when no value provided`() {
        val before = Instant.now()
        val timestamp = Timestamp()
        val after = Instant.now()

        assertTrue(timestamp.value >= before && timestamp.value <= after)
    }

    @Test
    fun `should create a Timestamp with provided instant`() {
        val instant = Instant.parse("2023-01-01T12:00:00Z")
        val timestamp = Timestamp(instant)

        assertEquals(instant, timestamp.value)
    }

    @Test
    fun `should consider identical values equal`() {
        val instant = Instant.parse("2023-01-01T12:00:00Z")
        val timestamp1 = Timestamp(instant)
        val timestamp2 = Timestamp(instant)

        assertEquals(timestamp1, timestamp2)
        assertEquals(timestamp1.hashCode(), timestamp2.hashCode())
    }

    @Test
    fun `should consider different values not equal`() {
        val timestamp1 = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))
        val timestamp2 = Timestamp(Instant.parse("2023-01-02T12:00:00Z"))

        assertNotEquals(timestamp1, timestamp2)
        assertNotEquals(timestamp1.hashCode(), timestamp2.hashCode())
    }

    @Test
    fun `should implement comparable interface correctly`() {
        val earlier = Timestamp(Instant.parse("2023-01-01T12:00:00Z"))
        val later = Timestamp(Instant.parse("2023-01-02T12:00:00Z"))

        assertTrue(earlier < later)
        assertTrue(later > earlier)
    }
}
