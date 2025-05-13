package org.codingkata.socialnetwork

class TimelineService {
    private val messages: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val subscribers: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun postMessage(authorUserId: String, messageContent: String) {
        this.messages.getOrPut(authorUserId, { mutableListOf() }).add(messageContent)

        subscribers.getOrPut(authorUserId, { mutableListOf() }).forEach {
            this.messages.getOrPut(it, { mutableListOf() }).add(messageContent)
        }
    }

    fun getAllMessages(authorUserId: String, requesterUserId: String = authorUserId): List<String> {
        return this.messages.getOrPut(authorUserId, { mutableListOf() })
    }

    fun subscribe(subscriberUserId: String, targetTimelineUserId: String) {
        subscribers.getOrPut(targetTimelineUserId, { mutableListOf() }).add(subscriberUserId)
    }

}