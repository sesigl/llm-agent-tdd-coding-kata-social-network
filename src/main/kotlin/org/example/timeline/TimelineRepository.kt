package org.example.timeline

import org.example.UserId

class TimelineRepository {

    private val timelineMessages = mutableMapOf<UserId, Timeline>()

    fun findOrCreateTimelineFor(userId: UserId): Timeline {
        return timelineMessages.getOrPut(userId) { Timeline(ownerUserId = userId) }
    }
}