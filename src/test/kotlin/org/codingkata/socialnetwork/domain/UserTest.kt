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
}
