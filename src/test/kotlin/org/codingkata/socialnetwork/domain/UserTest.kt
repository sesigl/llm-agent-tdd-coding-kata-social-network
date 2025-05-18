package org.codingkata.socialnetwork.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class UserTest {
    @Test
    fun `should create a user with a username`() {
        val username = "alice"
        val user = User(username)

        assertEquals(username, user.username)
    }

    @Test
    fun `users with same username should be equal`() {
        val username = "alice"

        val user1 = User(username)
        val user2 = User(username)

        assertEquals(user1, user2)
        assertEquals(user1.hashCode(), user2.hashCode())
    }
}
