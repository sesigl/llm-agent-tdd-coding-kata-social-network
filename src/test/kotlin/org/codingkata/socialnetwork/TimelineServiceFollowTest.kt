package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.FollowRepository
import org.codingkata.socialnetwork.repository.TimelineRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TimelineServiceFollowTest {
    @Test
    fun `should add follow relationship`() {
        val timelineRepository = TimelineRepository()
        val followRepository = FollowRepository()
        val service = TimelineService(timelineRepository, followRepository)
        val follower = UserId("alice")
        val followee = UserId("bob")

        service.followUser(follower, followee)

        assertTrue(followRepository.isFollowing(follower, followee))
    }

    @Test
    fun `should remove follow relationship`() {
        val timelineRepository = TimelineRepository()
        val followRepository = FollowRepository()
        val service = TimelineService(timelineRepository, followRepository)
        val follower = UserId("alice")
        val followee = UserId("bob")

        service.followUser(follower, followee)
        service.unfollowUser(follower, followee)

        assertFalse(followRepository.isFollowing(follower, followee))
    }

    @Test
    fun `should get users followed by a user`() {
        val timelineRepository = TimelineRepository()
        val followRepository = FollowRepository()
        val service = TimelineService(timelineRepository, followRepository)
        val alice = UserId("alice")
        val bob = UserId("bob")
        val charlie = UserId("charlie")

        service.followUser(alice, bob)
        service.followUser(alice, charlie)

        val followings = service.getFollowing(alice)

        assertEquals(2, followings.size)
        assertTrue(followings.contains(bob))
        assertTrue(followings.contains(charlie))
    }

    @Test
    fun `should not allow user to follow themselves`() {
        val timelineRepository = TimelineRepository()
        val followRepository = FollowRepository()
        val service = TimelineService(timelineRepository, followRepository)
        val alice = UserId("alice")

        try {
            service.followUser(alice, alice)
            assert(false) { "Expected exception was not thrown" }
        } catch (e: IllegalArgumentException) {
            // Expected exception
        }

        assertFalse(followRepository.isFollowing(alice, alice))
    }
}
