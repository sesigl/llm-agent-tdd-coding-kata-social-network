package org.codingkata.socialnetwork

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserFollowingTest {
    @Test
    fun `user following is created with a user`() {
        val user = User("alice")
        val userFollowing = UserFollowing(user)

        assertEquals(user, userFollowing.user)
        assertTrue(userFollowing.getFollowedUsers().isEmpty())
    }

    @Test
    fun `user can follow another user`() {
        val alice = User("alice")
        val bob = User("bob")

        val userFollowing = UserFollowing(alice)
        userFollowing.follow(bob)

        val followedUsers = userFollowing.getFollowedUsers()
        assertEquals(1, followedUsers.size)
        assertEquals(bob, followedUsers[0])
    }

    @Test
    fun `user can follow multiple users`() {
        val alice = User("alice")
        val bob = User("bob")
        val charlie = User("charlie")

        val userFollowing = UserFollowing(alice)
        userFollowing.follow(bob)
        userFollowing.follow(charlie)

        val followedUsers = userFollowing.getFollowedUsers()
        assertEquals(2, followedUsers.size)
        assertTrue(followedUsers.contains(bob))
        assertTrue(followedUsers.contains(charlie))
    }

    @Test
    fun `user cannot follow the same user twice`() {
        val alice = User("alice")
        val bob = User("bob")

        val userFollowing = UserFollowing(alice)
        userFollowing.follow(bob)
        userFollowing.follow(bob)

        val followedUsers = userFollowing.getFollowedUsers()
        assertEquals(1, followedUsers.size)
    }

    @Test
    fun `user following stores Following relationships`() {
        val alice = User("alice")
        val bob = User("bob")

        val userFollowing = UserFollowing(alice)
        userFollowing.follow(bob)

        val followings = userFollowing.getFollowings()
        assertEquals(1, followings.size)
        assertEquals(Following(alice, bob), followings[0])
    }
}
