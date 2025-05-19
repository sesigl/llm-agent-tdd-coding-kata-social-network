package org.codingkata.socialnetwork

import java.time.Instant

class TimelineService {
    private val timelines: MutableMap<User, Timeline> = mutableMapOf()

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

    private fun getOrCreateTimeline(user: User): Timeline = timelines.getOrPut(user) { Timeline(user) }
}
