package org.codingkata.socialnetwork

class TimelineService {
    private var timelines: MutableMap<String, Timeline> = mutableMapOf()

    fun publishMessage(messageContent: String, userId: String) {
        timelines.getOrPut(userId, { Timeline() })
            .publishMessage(messageContent)
    }

    fun getAllMessages(userId: String): List<String> {
        return timelines.getOrPut(userId, { Timeline() }).getAllMessages()
    }
}