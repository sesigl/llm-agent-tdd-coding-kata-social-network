package org.codingkata.socialnetwork

import org.codingkata.socialnetwork.repository.TimelineRepository

class TimelineService(
    private val repository: TimelineRepository,
) {
    fun postMessage(
        userId: UserId,
        content: String,
    ) {
        val message = Message.create(content, userId)
        repository.addMessage(message)
    }

    fun getTimeline(userId: UserId): List<Message> = repository.getMessagesChronologicallyDescending(userId)
}
