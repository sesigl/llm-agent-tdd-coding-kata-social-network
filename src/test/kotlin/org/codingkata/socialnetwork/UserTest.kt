package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
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
}
