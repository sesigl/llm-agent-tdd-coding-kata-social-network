package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.domain.message.model.Message
import java.time.Instant

class TimelineService {
    private val timelines: MutableMap<String, MutableList<Message>> = mutableMapOf()
    private val followingRelationships: MutableMap<String, MutableSet<String>> = mutableMapOf()

    fun post(
        username: String,
        message: String,
    ) {
        val userTimeline = timelines.getOrPut(username) { mutableListOf() }
        userTimeline.add(Message(message, username, Instant.now()))
    }

    // Legacy methods for backward compatibility
    fun getMessages(username: String): List<String> = timelines[username]?.map { it.content } ?: emptyList()

    fun getMessagesWithTime(username: String): List<Pair<String, Instant>> =
        timelines[username]?.map { Pair(it.content, it.timestamp) } ?: emptyList()

    // New methods using domain model
    fun getTimeline(username: String): List<String> = getTimelineMessages(username).map { it.content }

    fun getTimelineWithTime(username: String): List<Pair<String, Instant>> =
        getTimelineMessages(username).map { Pair(it.content, it.timestamp) }

    fun getTimelineMessages(username: String): List<Message> = timelines[username]?.sortedByDescending { it.timestamp } ?: emptyList()

    // Following functionality
    fun follow(
        follower: String,
        followee: String,
    ) {
        // Prevent self-following
        if (follower == followee) {
            return
        }

        val userFollowing = followingRelationships.getOrPut(follower) { mutableSetOf() }
        userFollowing.add(followee)
    }

    fun getFollowing(username: String): Set<String> = followingRelationships[username]?.toSet() ?: emptySet()

    // Aggregated timeline
    fun getAggregatedTimeline(username: String): List<Message> {
        val following = getFollowing(username)
        if (following.isEmpty()) {
            return emptyList()
        }

        val aggregatedMessages = mutableListOf<Message>()

        // Get messages from all followed users
        following.forEach { followee ->
            timelines[followee]?.let { messages ->
                aggregatedMessages.addAll(messages)
            }
        }

        // Sort by timestamp, newest first
        return aggregatedMessages.sortedByDescending { it.timestamp }
    }

    // Mention functionality
    fun getMentionsFromMessage(message: Message): Set<String> {
        val mentionRegex = Regex("@([A-Za-z0-9_]+)")
        val matches = mentionRegex.findAll(message.content)

        return matches.map { it.groupValues[1] }.toSet()
    }
}
