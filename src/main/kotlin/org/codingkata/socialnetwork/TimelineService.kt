package org.codingkata.socialnetwork

import java.time.Instant

class TimelineService {
    private val userTimelines: MutableMap<User, MutableList<Message>> = mutableMapOf()

    fun postMessage(
        user: User,
        content: String,
    ) {
        val message = Message(content, user, Instant.now())
        val userTimeline = userTimelines.getOrPut(user) { mutableListOf() }
        userTimeline.add(message)
    }

    fun getTimelineForUser(user: User): List<Message> = userTimelines[user] ?: emptyList()
}
