package org.codingkata.socialnetwork


class TimelineRepository {
    private var messages: MutableMap<String, Timeline> = mutableMapOf()

    public fun findOrCreate(ownerUserId: String): Timeline {
        return messages.getOrPut(ownerUserId, { Timeline() })
    }
}