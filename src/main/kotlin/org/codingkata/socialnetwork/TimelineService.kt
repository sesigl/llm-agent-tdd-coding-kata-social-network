package org.codingkata.socialnetwork

class TimelineService {
    private val messages: MutableList<String> = mutableListOf()

    fun postMessage(authorUserId: String, messageContent: String) {
        this.messages.add(messageContent)
    }

    fun getAllMessages(authorUserId: String): List<String> {
        return this.messages
    }

}