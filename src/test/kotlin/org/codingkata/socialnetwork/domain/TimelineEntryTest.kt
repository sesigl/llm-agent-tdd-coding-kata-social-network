package org.codingkata.socialnetwork.domain

import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class TimelineEntryTest {
    @Test
    fun `should create a timeline entry with a message and timestamp`() {
        val user = User("alice")
        val message = Message("Hello, world!", user)
        val timestamp = Instant.now()

        val timelineEntry = TimelineEntry(message, timestamp)

        assertEquals(message, timelineEntry.message)
        assertEquals(timestamp, timelineEntry.timestamp)
    }
}
