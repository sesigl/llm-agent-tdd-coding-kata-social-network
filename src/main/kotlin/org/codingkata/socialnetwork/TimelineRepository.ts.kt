package org.codingkata.socialnetwork

class TimelineRepository {
    private val timelines: MutableMap<String, Timeline> = mutableMapOf()

    fun getOrCreateTimeline(userId: String): Timeline {
        return timelines.getOrPut(userId, { Timeline() })
    }
}