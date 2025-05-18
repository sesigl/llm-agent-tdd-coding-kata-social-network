package org.codingkata.socialnetwork.repository

import org.codingkata.socialnetwork.Message
import org.codingkata.socialnetwork.Timeline
import org.codingkata.socialnetwork.TimelineId
import org.codingkata.socialnetwork.TimelineQuery
import org.codingkata.socialnetwork.UserId
import java.util.concurrent.ConcurrentHashMap

class TimelineRepository {
    private val timelines = ConcurrentHashMap<TimelineId, Timeline>()

    fun getOrCreateTimeline(userId: UserId): Timeline {
        val timelineId = TimelineId(userId)
        return timelines.computeIfAbsent(timelineId) { Timeline(it) }
    }

    fun addMessage(message: Message) {
        val timeline = getOrCreateTimeline(message.author)
        timeline.addMessage(message)
    }

    fun getMessagesFor(userId: UserId): List<Message> {
        val timeline = getOrCreateTimeline(userId)
        return timeline.getMessages()
    }

    fun getMessagesChronologicallyDescending(userId: UserId): List<Message> = getMessagesFor(userId)

    fun getMessagesWithQuery(
        userId: UserId,
        query: TimelineQuery,
    ): List<Message> {
        val timeline = getOrCreateTimeline(userId)
        return timeline.getMessages(query)
    }
}
