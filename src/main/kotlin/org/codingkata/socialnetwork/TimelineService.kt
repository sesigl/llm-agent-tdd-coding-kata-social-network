package org.codingkata.socialnetwork

class TimelineService {
    private val timelines: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun post(
        username: String,
        message: String,
    ) {
        val userTimeline = timelines.getOrPut(username) { mutableListOf() }
        userTimeline.add(message)
    }

    fun getMessages(username: String): List<String> = timelines[username] ?: emptyList()
}
