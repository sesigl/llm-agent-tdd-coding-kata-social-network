package org.codingkata.socialnetwork

class TimelineService {
    private var timeline: Timeline = Timeline()

    fun publishMessage(messageContent: String, userId: String) {
        this.timeline.publishMessage(messageContent)
    }

    fun getAllMessages(userId: String): List<String> {
        return this.timeline.getAllMessages()
    }
}