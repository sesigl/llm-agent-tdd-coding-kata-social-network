package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class TimelineIdTest {
    @Test
    fun `should create a TimelineId from UserId`() {
        val userId = UserId("alice")
        val timelineId = TimelineId(userId)

        assertEquals(userId, timelineId.userId)
    }

    @Test
    fun `should consider identical values equal`() {
        val userId = UserId("alice")
        val timelineId1 = TimelineId(userId)
        val timelineId2 = TimelineId(userId)

        assertEquals(timelineId1, timelineId2)
        assertEquals(timelineId1.hashCode(), timelineId2.hashCode())
    }

    @Test
    fun `should consider different values not equal`() {
        val alice = UserId("alice")
        val bob = UserId("bob")
        val timelineId1 = TimelineId(alice)
        val timelineId2 = TimelineId(bob)

        assertNotEquals(timelineId1, timelineId2)
        assertNotEquals(timelineId1.hashCode(), timelineId2.hashCode())
    }

    @Test
    fun `should convert from UserId`() {
        val userId = UserId("alice")
        val timelineId = TimelineId.fromUserId(userId)

        assertEquals(userId, timelineId.userId)
    }
}
