package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    fun `creating a user with a valid username`() {
        val user = User("alice")
        assertEquals("alice", user.username)
    }

    @Test
    fun `users with same username are equal`() {
        val user1 = User("bob")
        val user2 = User("bob")

        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `users with different usernames are not equal`() {
        val user1 = User("alice")
        val user2 = User("bob")

        assertNotEquals(user1, user2)
        assertNotEquals(user1.hashCode(), user2.hashCode())
    }

    @Test
    fun `isSameUser returns true for same username`() {
        val user1 = User("alice")
        val user2 = User("alice")

        assertTrue(user1.isSameUser(user2))
    }

    @Test
    fun `isSameUser returns false for different username`() {
        val user1 = User("alice")
        val user2 = User("bob")

        assertFalse(user1.isSameUser(user2))
    }

    @Test
    fun `isNamed returns true for matching username`() {
        val user = User("alice")

        assertTrue(user.isNamed("alice"))
        assertFalse(user.isNamed("bob"))
    }

    @Test
    fun `getUsernameDisplay provides username with @ prefix`() {
        val user = User("alice")

        assertEquals("@alice", user.getUsernameDisplay())
    }
}
