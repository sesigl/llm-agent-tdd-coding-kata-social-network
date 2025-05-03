package org.codingkata.socialnetwork

class TimelineService {
    private var messageContents: List<String> = emptyList()

    fun publishMessage(messageContent: String, userId: String) {
        this.messageContents = this.messageContents + messageContent
    }

    fun getAllMessages(userId: String): List<String> {
        return this.messageContents
    }
}