package org.codingkata.socialnetwork

class TimelineService {
    private val messages: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun postMessage(authorUserId: String, messageContent: String) {
        this.messages.getOrPut(authorUserId, { mutableListOf() }).add(messageContent)
    }

    fun getAllMessages(authorUserId: String): List<String> {
        return this.messages.getOrPut(authorUserId, { mutableListOf() })
    }

}