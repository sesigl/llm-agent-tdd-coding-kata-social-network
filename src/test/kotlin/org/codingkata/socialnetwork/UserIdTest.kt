package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserIdTest {
    @Test
    fun `should create a UserId with a valid id`() {
        val userId = UserId("alice")

        assertEquals("alice", userId.value)
    }

    @Test
    fun `should not allow empty id`() {
        assertThrows<IllegalArgumentException> {
            UserId("")
        }
    }

    @Test
    fun `should not allow blank id`() {
        assertThrows<IllegalArgumentException> {
            UserId("   ")
        }
    }

    @Test
    fun `should consider identical values equal`() {
        val userId1 = UserId("alice")
        val userId2 = UserId("alice")

        assertEquals(userId1, userId2)
        assertEquals(userId1.hashCode(), userId2.hashCode())
    }

    @Test
    fun `should consider different values not equal`() {
        val userId1 = UserId("alice")
        val userId2 = UserId("bob")

        assertNotEquals(userId1, userId2)
        assertNotEquals(userId1.hashCode(), userId2.hashCode())
    }
}
