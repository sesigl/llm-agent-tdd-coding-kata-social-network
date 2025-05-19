package org.codingkata.socialnetwork

import java.time.Instant

class TimelineService {
    private val timelines: MutableMap<User, Timeline> = mutableMapOf()
    private val followings: MutableMap<User, MutableSet<User>> = mutableMapOf()

    fun postMessage(
        user: User,
        content: String,
    ) {
        val message = Message(content, user, Instant.now())
        val timeline = getOrCreateTimeline(user)
        timeline.addMessage(message)
    }

    fun getTimelineForUser(user: User): List<Message> = getOrCreateTimeline(user).getMessages()

    fun getTimelineForUserInReverseChronologicalOrder(user: User): List<Message> =
        getOrCreateTimeline(user).getMessagesInReverseChronologicalOrder()

    fun followUser(
        follower: User,
        userToFollow: User,
    ) {
        val usersFollowed = followings.getOrPut(follower) { mutableSetOf() }
        usersFollowed.add(userToFollow)
    }

    fun getFollowedUsers(user: User): List<User> = followings[user]?.toList() ?: emptyList()

    fun getAggregatedTimeline(user: User): List<Message> {
        val allMessages = mutableListOf<Message>()

        // Add user's own messages
        allMessages.addAll(getTimelineForUser(user))

        // Add messages from followed users
        val followedUsers = getFollowedUsers(user)
        followedUsers.forEach { followedUser ->
            allMessages.addAll(getTimelineForUser(followedUser))
        }

        // Sort by timestamp (newest first)
        return allMessages.sortedByDescending { it.timestamp }
    }

    private fun getOrCreateTimeline(user: User): Timeline = timelines.getOrPut(user) { Timeline(user) }
}
