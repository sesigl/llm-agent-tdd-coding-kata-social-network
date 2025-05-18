package org.codingkata.socialnetwork.repository

import org.codingkata.socialnetwork.Follow
import org.codingkata.socialnetwork.UserId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FollowRepositoryTest {
    @Test
    fun `should add follow relationship`() {
        val repository = FollowRepository()
        val follower = UserId("alice")
        val followee = UserId("bob")
        val follow = Follow(follower, followee)

        repository.addFollow(follow)

        assertTrue(repository.isFollowing(follower, followee))
    }

    @Test
    fun `should remove follow relationship`() {
        val repository = FollowRepository()
        val follower = UserId("alice")
        val followee = UserId("bob")
        val follow = Follow(follower, followee)

        repository.addFollow(follow)
        repository.removeFollow(follower, followee)

        assertFalse(repository.isFollowing(follower, followee))
    }

    @Test
    fun `should get followers for user`() {
        val repository = FollowRepository()
        val alice = UserId("alice")
        val bob = UserId("bob")
        val charlie = UserId("charlie")

        repository.addFollow(Follow(bob, alice))
        repository.addFollow(Follow(charlie, alice))

        val followers = repository.getFollowersOf(alice)

        assertEquals(2, followers.size)
        assertTrue(followers.contains(bob))
        assertTrue(followers.contains(charlie))
    }

    @Test
    fun `should get followees for user`() {
        val repository = FollowRepository()
        val alice = UserId("alice")
        val bob = UserId("bob")
        val charlie = UserId("charlie")

        repository.addFollow(Follow(alice, bob))
        repository.addFollow(Follow(alice, charlie))

        val followees = repository.getFollowedBy(alice)

        assertEquals(2, followees.size)
        assertTrue(followees.contains(bob))
        assertTrue(followees.contains(charlie))
    }

    @Test
    fun `should return empty list if user has no followers`() {
        val repository = FollowRepository()
        val alice = UserId("alice")

        val followers = repository.getFollowersOf(alice)

        assertTrue(followers.isEmpty())
    }

    @Test
    fun `should return empty list if user is not following anyone`() {
        val repository = FollowRepository()
        val alice = UserId("alice")

        val followees = repository.getFollowedBy(alice)

        assertTrue(followees.isEmpty())
    }
}
