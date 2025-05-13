package org.codingkata.socialnetwork

class TimelineService {
    private val messages: MutableMap<String, Timeline> = mutableMapOf()
    private val subscribers: MutableMap<String, MutableList<String>> = mutableMapOf()

    fun postMessage(authorUserId: String, messageContent: String) {
        this.messages.getOrPut(authorUserId, { Timeline() }).postMessage(messageContent)

        subscribers.getOrPut(authorUserId, { mutableListOf() }).forEach {
            this.messages.getOrPut(it, { Timeline() }).postMessage(messageContent)
        }
    }

    fun getAllMessages(authorUserId: String, requesterUserId: String = authorUserId): List<String> {
        return this.messages.getOrPut(authorUserId, { Timeline() }).getAllMessages()
    }

    fun subscribe(subscriberUserId: String, targetTimelineUserId: String) {
        subscribers.getOrPut(targetTimelineUserId, { mutableListOf() }).add(subscriberUserId)
    }

}