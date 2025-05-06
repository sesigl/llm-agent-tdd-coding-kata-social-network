package org.codingkata.socialnetwork

class TimelineService {
    private var messages: List<String> = emptyList()

    fun publishMessage(authorUserId: String, content: String) {
        this.messages = this.messages  + content
    }
    fun getMessages(userId: String): List<String> {
        return this.messages
    }
}