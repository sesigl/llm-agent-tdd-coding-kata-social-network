package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class FollowingTest {
    @Test
    fun `create following relationship between users`() {
        val follower = User("alice")
        val followee = User("bob")

        val following = Following(follower, followee)

        assertEquals(follower, following.follower)
        assertEquals(followee, following.followee)
    }

    @Test
    fun `following relationships with same follower and followee are equal`() {
        val follower = User("alice")
        val followee = User("bob")

        val following1 = Following(follower, followee)
        val following2 = Following(follower, followee)

        assertEquals(following1, following2)
        assertEquals(following1.hashCode(), following2.hashCode())
    }

    @Test
    fun `following relationships with different follower are not equal`() {
        val alice = User("alice")
        val bob = User("bob")
        val charlie = User("charlie")

        val following1 = Following(alice, charlie)
        val following2 = Following(bob, charlie)

        assertNotEquals(following1, following2)
    }

    @Test
    fun `following relationships with different followee are not equal`() {
        val alice = User("alice")
        val bob = User("bob")
        val charlie = User("charlie")

        val following1 = Following(alice, bob)
        val following2 = Following(alice, charlie)

        assertNotEquals(following1, following2)
    }
}
