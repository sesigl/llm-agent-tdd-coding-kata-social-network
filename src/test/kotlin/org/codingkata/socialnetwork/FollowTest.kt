package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FollowTest {
    @Test
    fun `should create a Follow relationship between users`() {
        val follower = UserId("alice")
        val followee = UserId("bob")
        val follow = Follow(follower, followee)

        assertEquals(follower, follow.follower)
        assertEquals(followee, follow.followee)
    }

    @Test
    fun `should not allow following self`() {
        val userId = UserId("alice")

        assertThrows<IllegalArgumentException> {
            Follow(userId, userId)
        }
    }

    @Test
    fun `should consider identical follow relationships equal`() {
        val follower = UserId("alice")
        val followee = UserId("bob")
        val timestamp = Timestamp()

        val follow1 = Follow(follower, followee, timestamp)
        val follow2 = Follow(follower, followee, timestamp)

        assertEquals(follow1, follow2)
        assertEquals(follow1.hashCode(), follow2.hashCode())
    }

    @Test
    fun `should consider different follow relationships not equal`() {
        val alice = UserId("alice")
        val bob = UserId("bob")
        val charlie = UserId("charlie")

        val follow1 = Follow(alice, bob)
        val follow2 = Follow(alice, charlie)
        val follow3 = Follow(bob, alice)

        assertNotEquals(follow1, follow2)
        assertNotEquals(follow1, follow3)
        assertNotEquals(follow2, follow3)
    }

    @Test
    fun `should create follow relationship with timestamp`() {
        val follower = UserId("alice")
        val followee = UserId("bob")
        val timestamp = Timestamp()

        val follow = Follow(follower, followee, timestamp)

        assertEquals(timestamp, follow.timestamp)
    }
}
