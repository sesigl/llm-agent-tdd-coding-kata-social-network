package org.codingkata.socialnetwork.repository

import org.codingkata.socialnetwork.Follow
import org.codingkata.socialnetwork.UserId
import java.util.concurrent.ConcurrentHashMap

class FollowRepository {
    private val followsByFollower = ConcurrentHashMap<UserId, MutableSet<UserId>>()
    private val followersByFollowee = ConcurrentHashMap<UserId, MutableSet<UserId>>()

    fun addFollow(follow: Follow) {
        followsByFollower
            .computeIfAbsent(follow.follower) { mutableSetOf() }
            .add(follow.followee)

        followersByFollowee
            .computeIfAbsent(follow.followee) { mutableSetOf() }
            .add(follow.follower)
    }

    fun removeFollow(
        follower: UserId,
        followee: UserId,
    ) {
        followsByFollower[follower]?.remove(followee)
        followersByFollowee[followee]?.remove(follower)
    }

    fun isFollowing(
        follower: UserId,
        followee: UserId,
    ): Boolean = followsByFollower[follower]?.contains(followee) ?: false

    fun getFollowersOf(user: UserId): Set<UserId> = followersByFollowee[user]?.toSet() ?: emptySet()

    fun getFollowedBy(user: UserId): Set<UserId> = followsByFollower[user]?.toSet() ?: emptySet()
}
