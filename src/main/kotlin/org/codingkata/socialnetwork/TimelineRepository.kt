package org.codingkata.socialnetwork

class TimelineRepository {

    private var timelines: MutableMap<String, Timeline> = mutableMapOf()

    fun findBy(userId: String): Timeline {
        return timelines.getOrPut(userId, { Timeline() })
    }

}