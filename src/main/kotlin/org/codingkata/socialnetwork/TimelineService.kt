package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.FollowRepository
import org.codingkata.socialnetwork.repository.TimelineRepository

class TimelineService(
    private val timelineRepository: TimelineRepository = TimelineRepository(),
    private val followRepository: FollowRepository? = FollowRepository(),
) {
    fun postMessage(
        userId: UserId,
        content: String,
    ) {
        val message = Message.create(content, userId)
        timelineRepository.addMessage(message)
    }

    fun getTimeline(userId: UserId): List<Message> = timelineRepository.getMessagesChronologicallyDescending(userId)

    fun getTimeline(
        userId: UserId,
        query: TimelineQuery,
    ): List<Message> = timelineRepository.getMessagesWithQuery(userId, query)

    fun followUser(
        follower: UserId,
        followee: UserId,
    ) {
        requireNotNull(followRepository) { "FollowRepository is required for follow operations" }
        val follow = Follow(follower, followee)
        followRepository.addFollow(follow)
    }

    fun unfollowUser(
        follower: UserId,
        followee: UserId,
    ) {
        requireNotNull(followRepository) { "FollowRepository is required for follow operations" }
        followRepository.removeFollow(follower, followee)
    }

    fun getFollowing(userId: UserId): Set<UserId> {
        requireNotNull(followRepository) { "FollowRepository is required for follow operations" }
        return followRepository.getFollowedBy(userId)
    }
}
