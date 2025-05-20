package org.codingkata.socialnetwork

import java.time.Instant

class TimelineService {
    private val timelines: MutableMap<String, MutableList<Pair<String, Instant>>> = mutableMapOf()

    fun post(
        username: String,
        message: String,
    ) {
        val userTimeline = timelines.getOrPut(username) { mutableListOf() }
        userTimeline.add(Pair(message, Instant.now()))
    }

    fun getMessages(username: String): List<String> = timelines[username]?.map { it.first } ?: emptyList()

    fun getMessagesWithTime(username: String): List<Pair<String, Instant>> = timelines[username]?.toList() ?: emptyList()
}
