package org.codingkata.socialnetwork

class TimelineService {
    private lateinit var messageContent: String

    fun publishMessage(messageContent: String, userId: String) {
        this.messageContent = messageContent
    }
}